package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.exception.APIexception;
import com.ecommerce.project.CategoryService.exception.ResourceNotFoundException;
import com.ecommerce.project.CategoryService.model.Cart;
import com.ecommerce.project.CategoryService.model.CartItem;
import com.ecommerce.project.CategoryService.model.Product;
import com.ecommerce.project.CategoryService.payload.CartDTO;
import com.ecommerce.project.CategoryService.payload.ProductDTO;
import com.ecommerce.project.CategoryService.repositories.CartItemRepository;
import com.ecommerce.project.CategoryService.repositories.CartRepository;
import com.ecommerce.project.CategoryService.repositories.ProductRepository;
import com.ecommerce.project.CategoryService.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIexception("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() == 0) {
            throw new APIexception(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIexception("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        cart.getCartItems().add(newCartItem);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

        cartItemRepository.save(newCartItem);

        product.setQuantity(product.getQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        List<ProductDTO> productStream = cartItems.stream()
                .map(item -> {
                    ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                }).collect(Collectors.toList());

        cartDTO.setProduct(productStream);

        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> cart = cartRepository.findAll();

        if (cart.isEmpty())
            throw new APIexception("No cart available");

        List<CartDTO> cartDTO = cart.stream()
                .map(eachCart -> {
                    CartDTO cartDto = modelMapper.map(eachCart, CartDTO.class);
                    List<ProductDTO> productDto = eachCart.getCartItems().stream()
                            .map(eachCartItem ->
                                    modelMapper.map(eachCartItem.getProduct(), ProductDTO.class)).toList();
                    cartDto.setProduct(productDto);
                    return cartDto;
                }).toList();

//        List<CartItem> cartItems = cart.getCartItems();
//        List<ProductDTO> productStream = cart.stream()
//                .map(eachCart -> {
//                    eachCart.getCartItems().stream()
//                            .map(item -> {
//                                ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
//                                map.setQuantity(item.getQuantity());
//                                return map;
//                            })
//                            return ;
//                }).collect(Collectors.toList());

        return cartDTO;
    }

    @Override
    public CartDTO getUserCart(String email, Long cartId) {
//        Long userId = authUtil.loggedInUserId();
//        Cart cart = cartRepository.findByUserId(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("Cart", "UserId", userId));


        Cart cart = cartRepository.findByEmailIdAndCartId(email, cartId);
        if (cart == null)
            throw new ResourceNotFoundException("Cart", "CartId", cartId);

        CartDTO cartDto = modelMapper.map(cart, CartDTO.class);

        cart.getCartItems().forEach(eachCartItem ->
                eachCartItem.getProduct().setQuantity(eachCartItem.getQuantity()));

        List<ProductDTO> productDTOs = cart.getCartItems()
                .stream()
                .map(eachCartItem -> modelMapper.map(eachCartItem.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());
        cartDto.setProduct(productDTOs);

        return cartDto;
    }

    @Transactional
    @Override
    public CartDTO updateProductQuantity(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0) {
            throw new APIexception(product.getProductName() + " is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIexception("Please, make an order of the " + product.getProductName()
                    + " less than or equal to the quantity " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIexception("Product " + product.getProductName() + " not available in the cart!!!");
        }
        // Calculate new quantity
        int newQuantity = cartItem.getQuantity() + quantity;
        // Validation to prevent negative quantities
        if (newQuantity < 0) {
            throw new APIexception("The resulting quantity cannot be negative.");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cartItem.setProductPrice(product.getSpecialPrice());

            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if (updatedItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productStream = cartItems.stream().map(item -> {
            ProductDTO prd = modelMapper.map(item.getProduct(), ProductDTO.class);
            prd.setQuantity(item.getQuantity());
            return prd;
        });


        cartDTO.setProduct(productStream.toList());
        return cartDTO;
    }

    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() -
                (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";

    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new APIexception("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);
    }


    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);

        return newCart;
    }
}

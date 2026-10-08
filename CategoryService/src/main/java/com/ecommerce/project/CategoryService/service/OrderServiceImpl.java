package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.exception.APIexception;
import com.ecommerce.project.CategoryService.exception.ResourceNotFoundException;
import com.ecommerce.project.CategoryService.model.*;
import com.ecommerce.project.CategoryService.payload.OrderDTO;
import com.ecommerce.project.CategoryService.payload.OrderItemDTO;
import com.ecommerce.project.CategoryService.repositories.*;
import com.ecommerce.project.CategoryService.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.border.Border;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
//        User user =authUtil.loggedInUser();
// getting cart from email
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new APIexception("Cart not available for the user with email: " + emailId);
        }
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));

//setting new order with payment info
        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted!!!!");
        order.setAddress(address);

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);

        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

//get items from cart into order item

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty())
            throw new APIexception("CartIs empty!!!");

        List<OrderItem> orderItems = cartItems.stream()
                .map(eachCart -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(eachCart.getProduct());
                    orderItem.setQuantity(eachCart.getQuantity());
                    orderItem.setDiscount(eachCart.getDiscount());
                    orderItem.setOrderedProductPrice(eachCart.getProductPrice());
                    orderItem.setOrder(savedOrder);
                    return orderItem;
                }).toList();

        orderItemRepository.saveAll(orderItems);

//updateing the stock

        cartItems.forEach(eachCartItem -> {
            Product product = eachCartItem.getProduct();
            product.setQuantity(product.getQuantity() - eachCartItem.getQuantity());
            productRepository.save(product);
//clear cart
            cartService.deleteProductFromCart(cart.getCartId(), eachCartItem.getProduct().getProductId());
        });


//order summary
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        List<OrderItemDTO> orderItemDTO = orderItems.stream()
                .map(eachOrderItem ->
                        modelMapper.map(eachOrderItem, OrderItemDTO.class))
                .collect(Collectors.toList());
        orderDTO.setOrderItems(orderItemDTO);
        orderDTO.setAddressId(addressId);

        return orderDTO;
    }
}

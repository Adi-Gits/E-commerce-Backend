package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.exception.APIexception;
import com.ecommerce.project.CategoryService.exception.ResourceNotFoundException;
import com.ecommerce.project.CategoryService.model.Cart;
import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.model.Product;
import com.ecommerce.project.CategoryService.payload.CartDTO;
import com.ecommerce.project.CategoryService.payload.ProductDTO;
import com.ecommerce.project.CategoryService.payload.ProductResponseDTO;
import com.ecommerce.project.CategoryService.repositories.CartRepository;
import com.ecommerce.project.CategoryService.repositories.CategoryRepository;
import com.ecommerce.project.CategoryService.repositories.ProductRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    FileUploadService fileService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;


    @Value("${project.path}")
    String path;

    //Adding the product with category
    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO newProduct) {
        //Problem with this is, it wont allow same product in other category also
//        Product existingProduct = productRepository.findByProductName(newProduct.getProductName());
//        if (existingProduct != null)
//            throw new APIexception("Product "+existingProduct.getProductName()+" Already exists");
        ProductDTO addedProductDTO = null;
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean productCheck = true;
        //fetching products for  this category
        List<Product> products = category.getProducts();
        for (Product check : products) {
            if (check.getProductName().equalsIgnoreCase(newProduct.getProductName())) {
                productCheck = false;
                break;
            }

        }

        if (productCheck) {
            Product product = modelMapper.map(newProduct, Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            product.setSpecialPrice(product.getPrice() -
                    (product.getDiscount() * 0.01 * product.getPrice()));
            addedProductDTO = modelMapper.map(productRepository.save(product), ProductDTO.class);
        } else {
            throw new APIexception("Product " + newProduct.getProductName() + " Already exists for category id: " + categoryId);
        }
        return addedProductDTO;
    }

    //Getting all the product
    @Override
    public ProductResponseDTO getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//        if(productRepository.findAll().isEmpty())
//            throw new APIexception("No product available");
        //sorting
        Sort sortingDetials = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        //pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortingDetials);
        Page<Product> pages = productRepository.findAll(pageDetails);
        List<Product> products = pages.getContent();
        //custom exception
        if (products.isEmpty())
            throw new APIexception("No products available");
        //setting product to product dto
        List<ProductDTO> productDTO = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        //setting dto to response
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setResponse(productDTO);
        productResponseDTO.setPageNumber(pages.getNumber());
        productResponseDTO.setPageSize(pages.getSize());
        productResponseDTO.setTotalElements(pages.getTotalElements());
        productResponseDTO.setTotalPages(pages.getTotalPages());
        productResponseDTO.setLastPage(pages.isLast());

        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductbyCategoryId(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //getting category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Sort sortingDetials = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        //pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortingDetials);
        Page<Product> pages = productRepository.findByCategory(category, pageDetails);
        List<Product> products = pages.getContent();
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product", "categoryId", categoryId);
        }

        //setting product to product dto
        List<ProductDTO> productDTO = products.stream()
                .map(product1 -> modelMapper.map(product1, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setResponse(productDTO);
        productResponseDTO.setPageNumber(pages.getNumber());
        productResponseDTO.setPageSize(pages.getSize());
        productResponseDTO.setTotalElements(pages.getTotalElements());
        productResponseDTO.setTotalPages(pages.getTotalPages());
        productResponseDTO.setLastPage(pages.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductNameByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
//        if (products.isEmpty())
//            throw new APIexception("Product not available for Keyword " + keyword);

        //Sorting
        Sort sortDetails = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetials = PageRequest.of(pageNumber, pageSize, sortDetails);
        Page<Product> pages = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetials);
        List<Product> products = pages.getContent();

        if (products.isEmpty())
            throw new APIexception("Product not available for Keyword " + keyword);

            List<ProductDTO> productDTO = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setResponse(productDTO);
        productResponseDTO.setPageNumber(pages.getNumber());
        productResponseDTO.setPageSize(pages.getSize());
        productResponseDTO.setTotalElements(pages.getTotalElements());
        productResponseDTO.setTotalPages(pages.getTotalPages());
        productResponseDTO.setLastPage(pages.isLast());
        return productResponseDTO;

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO updatedProduct) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(updatedProduct, Product.class);
        productFromDB.setProductName(product.getProductName());
//        product.setImage(updatedProduct.getImage());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setSpecialPrice(product.getPrice() - (product.getDiscount() * 0.01 * product.getPrice()));
//        product.setCategory(updatedProduct.get);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

            List<ProductDTO> products = cart.getCartItems().stream()
                    .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

            cartDTO.setProduct(products);

            return cartDTO;

        }).collect(Collectors.toList());

        cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

        return modelMapper.map(productRepository.save(productFromDB), ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        productRepository.deleteById(productId);

        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //fetcjing the product
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        //uploading the image and getting its name
//        String path = "images/";
        String imageName = fileService.uploadImage(path, image);

        //saving/updatating DB product with image details
        productFromDb.setImage(imageName);

        //saving the DBproduct
        Product updatedProduct = productRepository.save(productFromDb);

        //converting tp DTO
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }


}

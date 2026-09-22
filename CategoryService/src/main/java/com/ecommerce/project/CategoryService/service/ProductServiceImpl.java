package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.configuration.APIResponse;
import com.ecommerce.project.CategoryService.exception.APIexception;
import com.ecommerce.project.CategoryService.exception.ResourceNotFoundException;
import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.model.Product;
import com.ecommerce.project.CategoryService.payload.ProductDTO;
import com.ecommerce.project.CategoryService.payload.ProductResponseDTO;
import com.ecommerce.project.CategoryService.repositories.CategoryRepository;
import com.ecommerce.project.CategoryService.repositories.ProductRepository;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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

    //Adding the product with category
    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO newProduct) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() ->
                        new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = modelMapper.map(newProduct, Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        product.setSpecialPrice(product.getPrice() -
                (product.getDiscount() * 0.01 * product.getPrice()));
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    //Getting all the product
    @Override
    public ProductResponseDTO getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
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
            throw new APIexception("No category available");
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

        //testing
        for (Product p : products) {
            System.out.println("product -->" + p);
        }

        //setting product to product dto
        List<ProductDTO> productDTO = products.stream()
                .map(product1 -> modelMapper.map(product1, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setResponse(productDTO);
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO searchProductNameByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        if (products.isEmpty())
            throw new APIexception("Product not available for Keyword " + keyword);

        List<ProductDTO> productDTO = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setResponse(productDTO);
        return productResponseDTO;

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO updatedProduct) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        Product product = modelMapper.map(updatedProduct,Product.class);
        productFromDB.setProductName(product.getProductName());
//        product.setImage(updatedProduct.getImage());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setQuantity(product.getQuantity());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setSpecialPrice(product.getPrice() - (product.getDiscount() * 0.01 * product.getPrice()));
//        product.setCategory(updatedProduct.get);
        return modelMapper.map(productRepository.save(product), ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        boolean b = false;
        Product existingProduct = productRepository.findById(productId)
//                .orElseThrow(() ->new ResourceNotFoundException("Product","productId",productId));
                .orElseThrow(() -> new APIexception("Not found"));
        productRepository.deleteById(productId);

        return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        //getting the product
       Product productFromDB = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        //uplaoding the image - baically uplaod image aplaya kadun path ani image gheto, tyla randaom id generate karun upload karto
        //then tyacha file name aplayala return karto
        String path ="image/";
        String filename = uploadImage(path, image);

        //saving image to product
        productFromDB.setImage(filename);
        //saving product
       Product savedProduct = productRepository.save(productFromDB);
        //contverting  to DTO
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        //logic to create filename
        String originalFileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String  filePath = path + File.separator + fileName;

        //checking if path exists
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();//creating path

        //uploading
        Files.copy(file.getInputStream(), Paths.get(filePath));

        return fileName;
    }
}

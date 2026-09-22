package com.ecommerce.project.CategoryService.controller;

import com.ecommerce.project.CategoryService.configuration.AppConstants;
import com.ecommerce.project.CategoryService.payload.ProductDTO;
import com.ecommerce.project.CategoryService.payload.ProductResponseDTO;
import com.ecommerce.project.CategoryService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,
                                                 @RequestBody ProductDTO product) {
        return new ResponseEntity<>(productService.addProduct(categoryId, product), HttpStatus.CREATED);

    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.pageNumber, required = false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.productSortBy, required = false) String sortBy,
                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.sortOrder, required = false) String sortOrder
    ) {
        ProductResponseDTO productResponseDTO = productService.getAllProduct(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductbyCategoryId(@PathVariable Long categoryId,
                                                                     @RequestParam(name = "pageNumber", defaultValue = AppConstants.pageNumber, required = false) Integer pageNumber,
                                                                     @RequestParam(name = "pageSize", defaultValue = AppConstants.pageSize, required = false) Integer pageSize,
                                                                     @RequestParam(name = "sortBy", defaultValue = AppConstants.productSortBy, required = false) String sortBy,
                                                                     @RequestParam(name = "sortOrder", defaultValue = AppConstants.sortOrder, required = false) String sortOrder
    ) {
        ProductResponseDTO productResponseDTO = productService.getProductbyCategoryId(categoryId, pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/public/product/keyword/{keyword}")
    public ResponseEntity<ProductResponseDTO> getProductByKeyword(@PathVariable String keyword) {
        return new ResponseEntity<>(productService.searchProductNameByKeyword(keyword), HttpStatus.FOUND);
    }

    @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO updatedProduct,
                                                    @PathVariable Long productId) {
        return new ResponseEntity<>(productService.updateProduct(productId, updatedProduct), HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
       ProductDTO productDTO = productService.updateProductImage(productId,image);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);

    }
}

package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.model.Product;
import com.ecommerce.project.CategoryService.payload.ProductDTO;
import com.ecommerce.project.CategoryService.payload.ProductResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponseDTO getAllProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getProductbyCategoryId(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO searchProductNameByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, ProductDTO updatedProduct);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}

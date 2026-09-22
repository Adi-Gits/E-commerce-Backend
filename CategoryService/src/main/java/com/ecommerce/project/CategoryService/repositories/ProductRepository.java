package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategory(Category category, Pageable pageDetails);

    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}

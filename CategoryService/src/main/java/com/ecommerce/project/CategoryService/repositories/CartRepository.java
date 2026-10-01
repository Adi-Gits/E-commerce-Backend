package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}

package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.Cart;
import com.ecommerce.project.CategoryService.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}

package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

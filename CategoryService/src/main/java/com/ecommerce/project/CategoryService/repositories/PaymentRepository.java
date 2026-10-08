package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
}

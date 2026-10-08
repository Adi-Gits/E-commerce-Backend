package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}

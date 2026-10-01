package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByEmail(String email);
    Boolean existsByUserName(String username);

//    boolean existsByUsername(@NotBlank @Size(max=50, message="username cannot longer than 50 characters") String );
}

package com.ecommerce.project.CategoryService.repositories;

import com.ecommerce.project.CategoryService.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

//Jparepo takes entity datatype and primaryid datatype as input
public interface CategoryRepository extends JpaRepository<Category,Long>{
    Category findByCategoryName(@NotBlank(message = "Name cannot be blank") String categoryName);
}

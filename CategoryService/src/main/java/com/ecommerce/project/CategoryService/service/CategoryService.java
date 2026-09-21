package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.payload.CategoryDTO;
import com.ecommerce.project.CategoryService.payload.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO getAllCategories(Integer pgNumber, Integer pgSize, String sortBy, String sortOrder);
    CategoryDTO createNewCategory(CategoryDTO c);

    CategoryDTO deleteCategory(Long catId);

    CategoryDTO updateCategory(Long catid, CategoryDTO cate);
}

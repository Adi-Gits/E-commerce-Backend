package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void createNewCategory(Category c);

    String deleteCategory(Long catId);

    String updateCategory(Long catid, Category cate);
}

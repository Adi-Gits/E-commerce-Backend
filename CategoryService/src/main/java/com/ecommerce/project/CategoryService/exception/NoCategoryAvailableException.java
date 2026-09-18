package com.ecommerce.project.CategoryService.exception;

import com.ecommerce.project.CategoryService.model.Category;

import java.util.List;

public class NoCategoryAvailableException extends RuntimeException {
    public NoCategoryAvailableException(String message) {
        super(message);
    }
}

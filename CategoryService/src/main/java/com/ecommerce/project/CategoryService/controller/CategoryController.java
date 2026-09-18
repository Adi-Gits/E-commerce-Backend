package com.ecommerce.project.CategoryService.controller;

import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

@Autowired// field injection
private CategoryService catSer;

//Insted of field injection constructor injection is prefered.
    //no need of @Autowierd if construtor available
//    public CategoryController(CategoryService catSer) {
//        this.catSer = catSer;
//    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAlltheCategories() {
        return new ResponseEntity<>(catSer.getAllCategories(),HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<String> createNewCategory(@Valid @RequestBody Category newCategory) {
        catSer.createNewCategory(newCategory);
        return new ResponseEntity<>("Added new category", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<String> deleteCatagoryWithCatId(@PathVariable Long catId){
//        try{
//           String status = catSer.deleteCategory(catId);
//            return new ResponseEntity<>(status,HttpStatus.OK);
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
//        }

    //try-catch not requird since global handling
        return new ResponseEntity<>(catSer.deleteCategory(catId),HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{catId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long catId,@RequestBody Category cate){
//        try{
//            return new ResponseEntity<>(catSer.updateCategory(catId,cate),HttpStatus.OK);
//
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
//        }

        //try-catch not requird since global handling
        return new ResponseEntity<>(catSer.updateCategory(catId,cate),HttpStatus.OK);
    }
}

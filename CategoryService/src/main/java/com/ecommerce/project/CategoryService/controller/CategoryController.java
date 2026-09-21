package com.ecommerce.project.CategoryService.controller;

import com.ecommerce.project.CategoryService.configuration.AppConstants;
import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.payload.CategoryDTO;
import com.ecommerce.project.CategoryService.payload.CategoryResponseDTO;
import com.ecommerce.project.CategoryService.service.CategoryService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
    @Autowired
    private ModelMapper modelMapper;

    //trying out @Requestparam
    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(@RequestParam(name = "messge", defaultValue = "sala kuch to de!!!",required = true) String msg,
    @RequestParam(name = "rply", defaultValue = "Nai dena", required = true) String reply) {
        return new ResponseEntity <> ("Got your msg :" + msg+"\n"+reply,HttpStatus.OK);
    }

    //Insted of field injection constructor injection is prefered.
    //no need of @Autowierd if construtor available
//    public CategoryController(CategoryService catSer) {
//        this.catSer = catSer;
//    }
    @GetMapping("/public/categories")
    //setting default values here is not a good practice so we configured constants class
    public ResponseEntity<CategoryResponseDTO> getAlltheCategories(@RequestParam (name="pageNumber", defaultValue=AppConstants.pageNumber,required = false) Integer pgNumber,
                                                                   @RequestParam (name="pageSize", defaultValue=AppConstants.pageSize,required = false) Integer pgSize,
                                                                   @RequestParam (name="sortBy",defaultValue=AppConstants.sortBy,required = false) String sortBy,
                                                                   @RequestParam (name="sortOrder",defaultValue=AppConstants.sortOrder,required=false) String sortOrder) {
        CategoryResponseDTO responseDTO = catSer.getAllCategories(pgNumber,pgSize,sortBy,sortOrder);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createNewCategory(@Valid @RequestBody CategoryDTO newCategory) {
//        CategoryDTO categoryDTO = modelMapper.map(newCategory, CategoryDTO.class);
        return new ResponseEntity<>(catSer.createNewCategory(newCategory), HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public ResponseEntity<CategoryDTO> deleteCatagoryWithCatId(@PathVariable Long catId) {
//        try{
//           String status = catSer.deleteCategory(catId);
//            return new ResponseEntity<>(status,HttpStatus.OK);
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
//        }

        //try-catch not requird since global handling
        return new ResponseEntity<>(catSer.deleteCategory(catId), HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{catId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long catId,@Valid @RequestBody CategoryDTO cate) {
//        try{
//            return new ResponseEntity<>(catSer.updateCategory(catId,cate),HttpStatus.OK);
//
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getReason(),e.getStatusCode());
//        }

        //try-catch not requird since global handling
        return new ResponseEntity<>(catSer.updateCategory(catId, cate), HttpStatus.OK);
    }
}

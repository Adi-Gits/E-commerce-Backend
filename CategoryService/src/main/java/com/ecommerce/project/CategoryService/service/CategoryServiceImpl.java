package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.exception.APIexception;
import com.ecommerce.project.CategoryService.exception.NoCategoryAvailableException;
import com.ecommerce.project.CategoryService.exception.ResourceNotFoundException;
import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

//    List<Category> categories = new ArrayList<>();
    Long nextCatId=1L;
    @Autowired
   private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        if(categoryRepository.findAll().isEmpty())
            throw new APIexception("No category available");
        else
            return categoryRepository.findAll();
//        return categories;
    }

    public Long categoryIdSequence(List<Category> c){
//        if(c.size()==0)
//        return 1L;
//        else
//        return c.get(c.size()-1).getCategoryId()+1L;
        return c.isEmpty() ? 1L : c.get(c.size()-1).getCategoryId()+1L;
    }


    @Override
    public void createNewCategory(Category c) {
//       if(c.getCategoryId()==null || c.getCategoryId().equals(""))
//        c.setCategoryId(categoryIdSequence(categories));
//        c.setCategoryId(nextCatId++);
//        categories.add(c);

        //implemneting custom exception for categaroy already available
      Category existingCategory =  categoryRepository.findByCategoryName(c.getCategoryName());
        if(!(existingCategory==null))
            throw new APIexception("Category "+c.getCategoryName()+" Already exists");
        else
            categoryRepository.save(c);
    }

    @Override
    public String deleteCategory(Long catId) {
        Category categories = categoryRepository.findById(catId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: "+catId+" doesnt exist"));//implemented custom exception

//       Category cat = categories.stream()
//                .filter(c -> c.getCategoryId().equals(catId))
//                .findFirst()
////                .orElse(null);
//               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
//       if(cat==null)
//           return "Category not found";

//       categories.remove(cat);
//        categoryRepository.delete(cat);
        //instead of above we can direced use deleteById
        categoryRepository.deleteById(catId);
       return "Category id: "+catId+" removed";
    }

    @Override
    public String updateCategory(Long catid, Category newCategory) {
//        List<Category> categories = categoryRepository.findAll();
//       Category cat = categories.stream()
//                .filter(c -> c.getCategoryId().equals(catid))
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));
//        cat.setCategoryName(cate.getCategoryName());
        Category oldCategory = categoryRepository.findById(catid)
                .orElseThrow(() -> new ResourceNotFoundException("Category","CategoryId",catid));
        newCategory.setCategoryId(catid);
//        cate.setCategoryName(cat.getCategoryName());
        categoryRepository.save(newCategory);
        return "Category with category id: "+catid+" updated successfully!";
    }
}

package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.exception.APIexception;
import com.ecommerce.project.CategoryService.exception.ResourceNotFoundException;
import com.ecommerce.project.CategoryService.model.Category;
import com.ecommerce.project.CategoryService.payload.CategoryDTO;
import com.ecommerce.project.CategoryService.payload.CategoryResponseDTO;
import com.ecommerce.project.CategoryService.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    //    List<Category> categories = new ArrayList<>();
//    Long nextCatId=1L;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelmapper;

    @Override
    public CategoryResponseDTO getAllCategories(Integer pgNumber,Integer pgSize,String sortBy, String sortOrder) {
        //****very imp
        Sort sorting = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pagedetails = PageRequest.of(pgNumber,pgSize,sorting);
        Page<Category> pageCategory = categoryRepository.findAll(pagedetails);//returns us Pages of entity
        List<Category> categories = pageCategory.getContent();//returns list of entity from page

        CategoryResponseDTO responseDTO = new CategoryResponseDTO();

        if (categories.isEmpty()) {
            throw new APIexception("No category available");
        } else {
            //Return list of Category
//            List<Category> categories = categoryRepository.findAll();

            //Each Category into CategoryDto
            List<CategoryDTO> catgoryDTO = categories.stream()
                    .map(category -> modelmapper.map(category, CategoryDTO.class))
                    .toList();
//Alternative For loop
//            List<CategoryDTO> catgoryDTO = new ArrayList<>();
//            for(Category c:categories){
//                catgoryDTO.add(modelmapper.map(c, CategoryDTO.class));
//            }

            //Passing list of dto to responseDto
            responseDTO.setContent(catgoryDTO);
            responseDTO.setPageNumber(pageCategory.getNumber());
            responseDTO.setPageSize(pageCategory.getSize());
            responseDTO.setTotalElements(pageCategory.getTotalElements());
            responseDTO.setTotalPages(pageCategory.getTotalPages());
            responseDTO.setLastPage(pageCategory.isLast());
            return responseDTO;
        }
//        return categories;
    }

    //  public Long categoryIdSequence(List<Category> c){
//        if(c.size()==0)
//        return 1L;
//        else
//        return c.get(c.size()-1).getCategoryId()+1L;
    //       return c.isEmpty() ? 1L : c.get(c.size()-1).getCategoryId()+1L;
    //   }


    @Override
    public CategoryDTO createNewCategory(CategoryDTO categoryDTO) {
//       if(c.getCategoryId()==null || c.getCategoryId().equals(""))
//        c.setCategoryId(categoryIdSequence(categories));
//        c.setCategoryId(nextCatId++);
//        categories.add(c);

        //implemneting custom exception for categaroy already available
        Category category = modelmapper.map(categoryDTO,Category.class);
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null)
            throw new APIexception("Category " + category.getCategoryName() + " Already exists");

        categoryDTO = modelmapper.map(categoryRepository.save(category), CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long catId) {
        Category categories = categoryRepository.findById(catId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: " + catId + " doesnt exist"));//implemented custom exception

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
        return modelmapper.map(categories, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long catid, CategoryDTO newCategory) {
//        List<Category> categories = categoryRepository.findAll();
//       Category cat = categories.stream()
//                .filter(c -> c.getCategoryId().equals(catid))
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));
//        cat.setCategoryName(cate.getCategoryName());
        Category oldCategory = categoryRepository.findById(catid)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", catid));

        Category category = modelmapper.map(newCategory,Category.class);
        category.setCategoryId(catid);
//        cate.setCategoryName(cat.getCategoryName());
        return modelmapper.map(categoryRepository.save(category), CategoryDTO.class);
//        return "Category with category id: " + catid + " updated successfully!";
    }


}

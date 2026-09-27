package com.ecommerce.project.CategoryService.model;

//import jakarta.persistence.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToMany(mappedBy = "category")
    private Long categoryId;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 5, message = "Kindly add category with atleast 5 chars")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products;

//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName !=null ? categoryName.trim() :null;
//    }
}

package com.ecommerce.project.CategoryService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.stereotype.Service;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, message = "Kindly provide atleast 3 characters for Product name")
    private String productName;

    @NotBlank(message = "description cannot be blank")
    @Size(min=6, message = "Kindly input atleast 6 characters for description")
    private String description;
    private String image;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;
}

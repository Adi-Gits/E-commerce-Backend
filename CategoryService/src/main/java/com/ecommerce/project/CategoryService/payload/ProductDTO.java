package com.ecommerce.project.CategoryService.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

   private Long productId;
   @NotBlank(message = "Product name cannot be blank")
   @Size(min = 3, message = "Kindly provide atleast 3 characters for Product name")
   private String productName;
   private String image;
   @NotBlank(message = "description cannot be blank")
   @Size(min=6, message = "Kindly input atleast 6 characters for description")
   private String description;
   private Integer quantity;
   private double discount;
   private Double specialPrice;
   private double price;
}

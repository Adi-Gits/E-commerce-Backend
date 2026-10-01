package com.ecommerce.project.CategoryService.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cartDto;
    private ProductDTO productDto;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}

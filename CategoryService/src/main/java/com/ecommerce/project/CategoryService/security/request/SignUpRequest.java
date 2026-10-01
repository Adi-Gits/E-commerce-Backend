package com.ecommerce.project.CategoryService.security.request;

import com.ecommerce.project.CategoryService.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpRequest {
    @NotBlank
    @Size(max=50, message="username cannot longer than 50 characters")
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(max=20, message="upassword cannot longer than 20 characters")
    private String password;
    private Set<String> role;
}

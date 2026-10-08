package com.ecommerce.project.CategoryService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min=3, message="min 3 characters required for building name")
    private String buildingName;

    @NotBlank
    @Size(min=3, message="min 3 characters required for street")
    private String street;

    @NotBlank
    @Size(min=3, message="min 2 characters required for city")
    private String city;

    @NotBlank
    @Size(min=3, message="min 3 characters required for state")
    private String state;

    @NotBlank
    @Size(min=3, message="min 3 characters required for country")
    private String country;

    @NotBlank
    @Size(min=6, message="min 6 characters required for zipcode")
    private String zipcode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String buildingName, String street, String city, String state, String country, String zipcode) {
        this.buildingName = buildingName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }
}


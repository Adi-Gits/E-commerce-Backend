package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.model.User;
import com.ecommerce.project.CategoryService.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddress(User user);

    AddressDTO updateAddress(Long addressId,AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}

package com.ecommerce.project.CategoryService.controller;

import com.ecommerce.project.CategoryService.model.User;
import com.ecommerce.project.CategoryService.payload.AddressDTO;
import com.ecommerce.project.CategoryService.service.AddressService;
import com.ecommerce.project.CategoryService.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    AddressService addressService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO newAddDTO = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(newAddDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> allAdd = addressService.getAllAddresses();
        return new ResponseEntity<List<AddressDTO>>(allAdd, HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        return new ResponseEntity<>(
                addressService.getAddressById(addressId), HttpStatus.OK);
    }

    @GetMapping("users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddress() {
        User user = authUtil.loggedInUser();
        List<AddressDTO> userAddresses = addressService.getUserAddress(user);
        return new ResponseEntity<>(userAddresses, HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId
                                                     , @RequestBody AddressDTO addressDTO) {
        AddressDTO newAddressDTO = addressService.updateAddress(addressId,addressDTO);
        return new ResponseEntity<>(newAddressDTO, HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}

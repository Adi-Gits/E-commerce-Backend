package com.ecommerce.project.CategoryService.service;

import com.ecommerce.project.CategoryService.exception.APIexception;
import com.ecommerce.project.CategoryService.exception.ResourceNotFoundException;
import com.ecommerce.project.CategoryService.model.Address;
import com.ecommerce.project.CategoryService.model.User;
import com.ecommerce.project.CategoryService.payload.AddressDTO;
import com.ecommerce.project.CategoryService.repositories.AddressRepository;
import com.ecommerce.project.CategoryService.repositories.UserRepository;
import com.ecommerce.project.CategoryService.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        User user = authUtil.loggedInUser();

//saving add in user
        List<Address> addresses = user.getAddresses();
        addresses.add(address);
        user.setAddresses(addresses);

//saving user in add
        address.setUser(user);
        addressRepository.save(address);
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> add = addressRepository.findAll();
//       List<AddressDTO> addDTO = add.stream()
//               .map(eachAddress -> modelMapper.map(eachAddress,AddressDTO.class))
//               .collect(Collectors.toList());

        List<AddressDTO> addDTO = new ArrayList<>();
        for (Address eachAddress : add) {
            AddressDTO dto = modelMapper.map(eachAddress, AddressDTO.class);
            addDTO.add(dto);
        }
        return addDTO;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address addressFromDb = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));

        return modelMapper.map(addressFromDb, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddress(User user) {
        List<Address> userAddress = user.getAddresses();

        if (userAddress.isEmpty())
            throw new APIexception("Address not avaialable for this user");

        List<AddressDTO> userAddressDTO = userAddress.stream()
                .map(eachAdd -> modelMapper.map(eachAdd, AddressDTO.class))
                .collect(Collectors.toList());
        return userAddressDTO;
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));

        address.setCity(addressDTO.getCity());
        address.setBuildingName(addressDTO.getBuildingName());
        address.setCountry(addressDTO.getCountry());
        address.setState(addressDTO.getState());
        address.setStreet(addressDTO.getStreet());
        address.setZipcode(addressDTO.getZipcode());

        Address saveAdd = addressRepository.save(address);
//saving add of user
        User user = address.getUser();
        user.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
        //stream ni pan hote hai
//        List<Address> filterAdd = user.getAddresses().stream().
//                filter(eachAdd -> !eachAdd.getAddressId().equals(addressId))
//                .collect(Collectors.toList());
//        filterAdd.add(address);
//        user.setAddresses(filterAdd);

        user.getAddresses().add(address);

        userRepository.save(user);
        return modelMapper.map(saveAdd, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addFromDb = addressRepository.findById(addressId).
                orElseThrow(()->new ResourceNotFoundException("Address","AddressID",addressId));

        User user =addFromDb.getUser();
//        List<Address> filteredAdd = user.getAddresses().stream()
//                        .filter(eachAdd -> !eachAdd.getAddressId().equals(addressId))
//                                .collect(Collectors.toList());
//        user.setAddresses(filteredAdd);
        user.getAddresses().removeIf(add -> add.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addFromDb);
        return "delete address for addressId: "+addressId;
    }
}

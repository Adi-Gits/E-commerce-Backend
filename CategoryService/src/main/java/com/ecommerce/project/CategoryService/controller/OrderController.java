package com.ecommerce.project.CategoryService.controller;

import com.ecommerce.project.CategoryService.payload.OrderDTO;
import com.ecommerce.project.CategoryService.payload.OrderRequestDTO;
import com.ecommerce.project.CategoryService.service.OrderService;
import com.ecommerce.project.CategoryService.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    AuthUtil authutil;

    @Autowired
    OrderService orderService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                                  @RequestBody OrderRequestDTO orderRequestDTO) {
        String emailId = authutil.loggedInEmail();
        OrderDTO order = orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

}

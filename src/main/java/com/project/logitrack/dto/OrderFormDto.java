package com.project.logitrack.dto;

import java.util.List;

import com.project.logitrack.Entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFormDto {
    // Customer info
    private String customerName;
    private String contact;
    private String email;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
//    private User user;
    // List of products/items in the order
    private List<OrderItemFormDto> items;

}


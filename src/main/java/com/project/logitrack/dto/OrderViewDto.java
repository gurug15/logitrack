package com.project.logitrack.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderViewDto {
	private Long id;
    private String customername;
    private String customerphone;
    private String customeremail;
    private String customeraddress;
    private String city;
    private String state;
    private String postalcode;
    private String country;
    private LocalDate orderdate;
    private String status;
    private BigDecimal totalprice;
    private List<OrderItemDto> orderItems; // include order item DTOs if applicable
}

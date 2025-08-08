package com.project.logitrack.dto;

import java.util.List;

import com.project.logitrack.Entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFormDto {
    // Customer info
	@NotBlank(message = "Customer name is required")
    @Size(min = 3, max = 100, message = "Customer name must be between 3 and 100 characters")
    private String customerName;
	
	@NotBlank(message = "Contact number is required")
	@Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "Invalid contact phone format")
    private String contact;
	
	@NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;
	
	@NotBlank(message = "Address is required")
    private String address;
	
	@NotBlank(message = "City field is required")
    private String city;
	
	@NotBlank(message = "State field is required")
    private String state;
    
    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "\\d{6}", message = "Postal code must be exactly 6 digits")
    private String postalCode;
    private String country;

    @NotEmpty(message = "At least one order item is required")
    private List<OrderItemFormDto> items;

}


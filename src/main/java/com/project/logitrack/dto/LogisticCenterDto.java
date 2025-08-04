package com.project.logitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticCenterDto {
	
	private Long id;
	private String name;
    private String city;
    private String state;
    private String address;
    private String postalcode; 
    private String contactPhone;

}

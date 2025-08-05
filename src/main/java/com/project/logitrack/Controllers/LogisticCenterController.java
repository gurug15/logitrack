package com.project.logitrack.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.dto.LogisticCenterDto;
import com.project.logitrack.service.LogisticCenterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/logistic-centers")
public class LogisticCenterController {
	
	
	@Autowired
	LogisticCenterService logisticCenterService;
	
	@GetMapping
	public ResponseEntity<List<LogisticCenterDto>> getAllLogisticCenters(){
		
		return ResponseEntity.ok(logisticCenterService.getallCenters());
	}
	
	@PostMapping
    public ResponseEntity<LogisticCenterDto> createLogisticCenter(@Valid @RequestBody LogisticCenterDto centerDto) {
        LogisticCenterDto createdCenter = logisticCenterService.createCenter(centerDto);
        return new ResponseEntity<>(createdCenter, HttpStatus.CREATED);
    }
}

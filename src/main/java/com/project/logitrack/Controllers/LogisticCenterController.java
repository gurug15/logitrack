package com.project.logitrack.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.dto.LogisticCenterDto;
import com.project.logitrack.service.LogisticCenterService;

@RestController
@RequestMapping("/logistic-centers")
public class LogisticCenterController {
	
	
	@Autowired
	LogisticCenterService logisticCenterService;
	
	@GetMapping
	public ResponseEntity<List<LogisticCenterDto>> getAllLogisticCenters(){
		
		return ResponseEntity.ok(logisticCenterService.getallCenters());
	}
	
}

package com.project.logitrack.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.logitrack.dto.RolesDto;
import com.project.logitrack.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RolesController {

	@Autowired
	RoleService roleService;
	
	@GetMapping
	public ResponseEntity<List<RolesDto>> getRoles(){
		return ResponseEntity.ok(roleService.getRoles());
	}
}

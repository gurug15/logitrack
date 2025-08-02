package com.project.logitrack.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.Roles;
import com.project.logitrack.Mappers.RolesMapper;
import com.project.logitrack.dto.RolesDto;
import com.project.logitrack.repositories.RolesRepository;


@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	RolesRepository roleRepo;
		
	@Override
	public List<RolesDto> getRoles() {
	    List<Roles> roles = roleRepo.findAll();
	    return roles.stream()
	                .map(RolesMapper::toDto)
	                .collect(Collectors.toList());
	}


}

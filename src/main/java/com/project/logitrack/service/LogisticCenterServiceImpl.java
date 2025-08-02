package com.project.logitrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.logitrack.Mappers.LogisticCenterMapper;
import com.project.logitrack.dto.LogisticCenterDto;
import com.project.logitrack.repositories.LogisticCenterRepository;

@Service
public class LogisticCenterServiceImpl implements LogisticCenterService {

	@Autowired
	LogisticCenterRepository logisticCenterRepository;

	@Override
	public List<LogisticCenterDto> getallCenters() {
		// TODO Auto-generated method stub
		return logisticCenterRepository.findAll().stream().map(LogisticCenterMapper::toDto).toList();
	}
	
	
	
	
}

package com.project.logitrack.service;

import java.util.List;

import com.project.logitrack.Entity.LogisticCenter;
import com.project.logitrack.dto.LogisticCenterDto;

public interface LogisticCenterService {
	
	List<LogisticCenterDto> getallCenters();
	
	LogisticCenterDto createCenter(LogisticCenterDto centerDto);
	
	int getCenterFromPincode(String pincode);//it is added here
	List<Integer> findRoute(int sourceCenterId, int destinationCenterId);//It is added here
}

package com.project.logitrack.Mappers;

import com.project.logitrack.Entity.LogisticCenter;
import com.project.logitrack.dto.LogisticCenterDto;

public class LogisticCenterMapper {
	
    
    public static LogisticCenterDto toDto(LogisticCenter center) {
        if (center == null) {
            return null;
        }
        LogisticCenterDto dto = new LogisticCenterDto();
        dto.setId(center.getId());
        dto.setName(center.getName());
        dto.setCity(center.getCity());
        dto.setState(center.getState());
        dto.setAddress(center.getAddress());
        dto.setContactPhone(center.getContactPhone());
        dto.setPostalcode(center.getPostalcode());
        return dto;
    }

    public static LogisticCenter toEntity(LogisticCenterDto dto) {
        if (dto == null) {
            return null;
        }
        LogisticCenter center = new LogisticCenter();
        center.setId(dto.getId());
        center.setName(dto.getName());
        center.setCity(dto.getCity());
        center.setState(dto.getState());
        center.setAddress(dto.getAddress());
        center.setContactPhone(dto.getContactPhone());
        center.setPostalcode(dto.getPostalcode());
        return center;
    }
}

package com.project.logitrack.Mappers;

import com.project.logitrack.Entity.Roles;
import com.project.logitrack.dto.RolesDto;

public class RolesMapper {

    public static RolesDto toDto(Roles role) {
        if (role == null) {
            return null;
        }
        RolesDto dto = new RolesDto();
        dto.setId(role.getId());
        dto.setName(role.getRoleName());
        return dto;
    }

    public static Roles toEntity(RolesDto dto) {
        if (dto == null) {
            return null;
        }
        Roles role = new Roles();
        role.setId(dto.getId());
        role.setRoleName(dto.getName());
        return role;
    }
}

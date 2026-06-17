package com.hotel_erp.hotel_erp.modules.user;

import com.hotel_erp.hotel_erp.modules.employee.DepartmentEntity;
import com.hotel_erp.hotel_erp.modules.employee.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public UserDTO toDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setFullName(entity.getFullName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.isActive());
        dto.setRole(entity.getRole() != null ? entity.getRole().name() : null);
        dto.setDepartmentId(entity.getDepartment() != null ? entity.getDepartment().getId() : null);
        
        dto.setCreatedBy(entity.getCreatedBy());
        if (entity.getCreatedBy() != null) {
            userRepository.findById(entity.getCreatedBy())
                .ifPresent(u -> dto.setCreatedByName(u.getUsername()));
        }
        
        dto.setModifiedBy(entity.getModifiedBy());
        if (entity.getModifiedBy() != null) {
            userRepository.findById(entity.getModifiedBy())
                .ifPresent(u -> dto.setModifiedByName(u.getUsername()));
        }
        
        return dto;
    }

    public UserEntity toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setFullName(dto.getFullName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        entity.setActive(dto.isActive());
        if (dto.getRole() != null && !dto.getRole().isEmpty()) {
            entity.setRole(Role.valueOf(dto.getRole()));
        }
        // The instruction implies ignoring the department mapping from DTO to Entity.
        // In a manual mapper, this means removing the logic that sets the department.
        // If a password hash field were present and needed ignoring, it would also be omitted.
        // The provided "Code Edit" snippet was syntactically incorrect for this manual mapper.
        // Therefore, the department setting logic is removed to "ignore" it.
        return entity;
    }
}

package com.hotel_erp.hotel_erp.modules.user;

import com.hotel_erp.hotel_erp.modules.employee.DepartmentEntity;
import com.hotel_erp.hotel_erp.modules.employee.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final com.hotel_erp.hotel_erp.modules.activity.ActivityLogService activityLogService;

    public UserEntity save(UserEntity user, String rawPassword, Long departmentId) {
        return save(user, rawPassword, departmentId, null);
    }

    public UserEntity save(UserEntity user, String rawPassword, Long departmentId, Long adminId) {
        if (rawPassword != null && !rawPassword.isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        if (departmentId != null) {
            user.setDepartment(departmentRepository.findById(departmentId).orElse(null));
        }
        if (adminId != null) {
            user.setCreatedBy(adminId);
        }
        UserEntity saved = userRepository.save(user);
        activityLogService.logActivity(adminId, "CREATE_USER", "Created user profile: " + saved.getUsername());
        return saved;
    }

    public UserEntity update(UserEntity existingUser, UserDTO updateDto) {
        return update(existingUser, updateDto, null);
    }

    public UserEntity update(UserEntity existingUser, UserDTO updateDto, Long adminId) {
        if (updateDto.getUsername() != null && !updateDto.getUsername().isEmpty()) {
            existingUser.setUsername(updateDto.getUsername());
        }
        existingUser.setFullName(updateDto.getFullName());
        existingUser.setEmail(updateDto.getEmail());
        existingUser.setPhoneNumber(updateDto.getPhoneNumber());
        existingUser.setActive(updateDto.isActive());
        if (updateDto.getRole() != null && !updateDto.getRole().isEmpty()) {
            existingUser.setRole(Role.valueOf(updateDto.getRole()));
        }
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(updateDto.getPassword()));
        }
        
        if (updateDto.getDepartmentId() != null) {
            existingUser.setDepartment(departmentRepository.findById(updateDto.getDepartmentId()).orElse(null));
        } else {
            existingUser.setDepartment(null);
        }
        
        if (adminId != null) {
            existingUser.setModifiedBy(adminId);
        }
        
        UserEntity updated = userRepository.save(existingUser);
        activityLogService.logActivity(adminId, "UPDATE_USER", "Updated user profile: " + updated.getUsername());
        return updated;
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}

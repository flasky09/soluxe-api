package com.hotel_erp.hotel_erp.modules.user;

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

    public UserEntity save(UserEntity user, String rawPassword) {
        if (rawPassword != null && !rawPassword.isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
        }
        return userRepository.save(user);
    }

    public UserEntity update(UserEntity existingUser, UserDTO updateDto) {
        existingUser.setFullName(updateDto.getFullName());
        existingUser.setEmail(updateDto.getEmail());
        existingUser.setPhoneNumber(updateDto.getPhoneNumber());
        existingUser.setActive(updateDto.isActive());
        if (updateDto.getRole() != null) {
            existingUser.setRole(Role.valueOf(updateDto.getRole()));
        }
        
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(updateDto.getPassword()));
        }
        
        return userRepository.save(existingUser);
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
}

package com.hotel_erp.hotel_erp.modules.user;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO, @RequestParam("userId") Long userId) {
        UserEntity userEntity = userMapper.toEntity(userDTO);
        UserEntity savedUser = userService.save(userEntity, userDTO.getPassword(), userDTO.getDepartmentId(), userId);
        return userMapper.toDto(savedUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO, @RequestParam("userId") Long userId) {
        UserEntity existingUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity updatedUser = userService.update(existingUser, userDTO, userId);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{userIdentifier}")
    public UserDTO getUserById(@PathVariable Long userIdentifier) {
        UserEntity userEntity = userService.findById(userIdentifier)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @GetMapping
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream()
            .map(userMapper::toDto)
            .toList();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HOTEL_ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }
}

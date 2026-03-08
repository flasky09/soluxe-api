package com.hotel_erp.hotel_erp.modules.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        UserEntity userEntity = userMapper.toEntity(userDTO);
        UserEntity savedUser = userService.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    @GetMapping("/{userIdentifier}")
    public UserDTO getUserById(@PathVariable Long userIdentifier) {
        UserEntity userEntity = userService.findById(userIdentifier)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(userEntity);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream()
            .map(userMapper::toDto)
            .toList();
    }
}

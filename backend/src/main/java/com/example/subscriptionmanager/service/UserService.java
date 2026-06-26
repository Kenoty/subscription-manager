package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.CreateUserRequest;
import com.example.subscriptionmanager.dto.request.UpdateUserRequest;
import com.example.subscriptionmanager.dto.response.UserResponse;
import com.example.subscriptionmanager.entity.Role;
import com.example.subscriptionmanager.entity.User;
import com.example.subscriptionmanager.mapper.UserMapper;
import com.example.subscriptionmanager.repository.RoleRepository;
import com.example.subscriptionmanager.repository.UserRepository;
import com.example.subscriptionmanager.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserResponse create(CreateUserRequest request) {
        Role role = roleRepository.findByName("user")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = userMapper.toEntity(request, role);
        user.setPasswordHash(request.getPassword()); // добавить хэширование

        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse getById() {
        return userMapper.toResponse(userRepository.findById(currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse update(UpdateUserRequest request) {
        User user = userRepository.findById(currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateEntity(user, request);

        return userMapper.toResponse(userRepository.save(user));
    }

    public void delete() {
        if(!userRepository.existsById(currentUserProvider.getCurrentUserId())) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(currentUserProvider.getCurrentUserId());
    }
}

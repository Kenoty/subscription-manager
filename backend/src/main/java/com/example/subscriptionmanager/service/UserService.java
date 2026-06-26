package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.ChangePasswordRequest;
import com.example.subscriptionmanager.dto.request.UpdateUserRequest;
import com.example.subscriptionmanager.dto.response.UserResponse;
import com.example.subscriptionmanager.entity.User;
import com.example.subscriptionmanager.exception.BadRequestException;
import com.example.subscriptionmanager.exception.ResourceNotFoundException;
import com.example.subscriptionmanager.mapper.UserMapper;
import com.example.subscriptionmanager.repository.UserRepository;
import com.example.subscriptionmanager.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getProfile() {
        return userMapper.toResponse(currentUserProvider.getCurrentUser());
    }

    public UserResponse update(UpdateUserRequest request) {
        User user = currentUserProvider.getCurrentUser();
        userMapper.updateEntity(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = currentUserProvider.getCurrentUser();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void delete() {
        userRepository.deleteById(currentUserProvider.getCurrentUserId());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    public UserResponse getUserById(Integer id) {
        return userMapper.toResponse(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}

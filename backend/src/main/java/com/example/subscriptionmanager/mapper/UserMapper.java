package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.request.RegisterRequest;
import com.example.subscriptionmanager.dto.request.UpdateUserRequest;
import com.example.subscriptionmanager.dto.response.UserResponse;
import com.example.subscriptionmanager.entity.Role;
import com.example.subscriptionmanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request, Role role) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        return user;
    }

    public void updateEntity(User user, UpdateUserRequest request) {
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().getName());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}

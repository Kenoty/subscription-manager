package com.example.subscriptionmanager.mapper;

import com.example.subscriptionmanager.dto.request.CreateUserRequest;
import com.example.subscriptionmanager.dto.request.UpdateUserRequest;
import com.example.subscriptionmanager.dto.response.UserResponse;
import com.example.subscriptionmanager.entity.Role;
import com.example.subscriptionmanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request, Role role) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        return user;
    }

    public void updateEntity(User user, UpdateUserRequest request) {
        if(request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if(request.getLastName() != null) user.setLastName(request.getLastName());
    }

    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}

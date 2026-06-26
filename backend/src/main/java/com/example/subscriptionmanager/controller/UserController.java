package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.request.CreateUserRequest;
import com.example.subscriptionmanager.dto.request.UpdateUserRequest;
import com.example.subscriptionmanager.dto.response.UserResponse;
import com.example.subscriptionmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @GetMapping
    public ResponseEntity<UserResponse> getById() {
        return ResponseEntity.ok(userService.getById());
    }

    @PatchMapping
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        userService.delete();
        return ResponseEntity.noContent().build();
    }
}

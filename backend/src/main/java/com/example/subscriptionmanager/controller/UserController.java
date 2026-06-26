package com.example.subscriptionmanager.controller;

import com.example.subscriptionmanager.dto.request.ChangePasswordRequest;
import com.example.subscriptionmanager.dto.request.UpdateUserRequest;
import com.example.subscriptionmanager.dto.response.UserResponse;
import com.example.subscriptionmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление профилем")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить свой профиль")
    public ResponseEntity<UserResponse> getProfile() {
        return ResponseEntity.ok(userService.getProfile());
    }

    @PatchMapping
    @Operation(summary = "Обновить профиль")
    public ResponseEntity<UserResponse> update(@Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(request));
    }

    @PatchMapping("/password")
    @Operation(summary = "Сменить пароль")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(summary = "Удалить аккаунт")
    public ResponseEntity<Void> delete() {
        userService.delete();
        return ResponseEntity.noContent().build();
    }
}

package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.LoginRequest;
import com.example.subscriptionmanager.dto.request.RegisterRequest;
import com.example.subscriptionmanager.dto.response.AuthResponse;
import com.example.subscriptionmanager.entity.Role;
import com.example.subscriptionmanager.entity.User;
import com.example.subscriptionmanager.exception.BadRequestException;
import com.example.subscriptionmanager.mapper.UserMapper;
import com.example.subscriptionmanager.repository.RoleRepository;
import com.example.subscriptionmanager.repository.UserRepository;
import com.example.subscriptionmanager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        Role role = roleRepository.findByName("user")
                .orElseThrow(() -> new BadRequestException("Role not found"));
        User user = userMapper.toEntity(request, role);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User not found"));
        return new AuthResponse(jwtService.generateToken(user));
    }
}

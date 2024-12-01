package com.nativenews.api.controller;

import com.nativenews.api.dto.UserRegistrationDTO;
import com.nativenews.api.dto.UserLoginDTO;
import com.nativenews.api.dto.AuthResponseDTO;
import com.nativenews.api.domain.model.User;
import com.nativenews.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        User registeredUser = userService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        String token = userService.authenticateUser(loginDTO);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
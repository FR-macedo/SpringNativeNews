package com.nativenews.api.services;

import com.nativenews.api.dto.UserRegistrationDTO;
import com.nativenews.api.dto.UserLoginDTO;
import com.nativenews.api.domain.model.User;
import com.nativenews.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("Usuário já existe");
        }

        User user = new User(
                registrationDTO.getUsername(),
                passwordEncoder.encode(registrationDTO.getPassword())
        );

        return userRepository.save(user);
    }

    public String authenticateUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return jwtService.generateToken(user);
    }
}
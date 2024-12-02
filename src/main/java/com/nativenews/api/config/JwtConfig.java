package com.nativenews.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}") // Tempo de expiração padrão: 24 horas
    private long jwtExpiration;

    @Bean
    public String jwtSecret() {
        return jwtSecret; // Centraliza a chave secreta
    }

    @Bean
    public long jwtExpiration() {
        return jwtExpiration; // Centraliza o tempo de expiração
    }
}

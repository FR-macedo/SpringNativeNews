package com.nativenews.api.services;


import com.nativenews.api.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())  // Define o usuário do token
                .setIssuedAt(new Date())         // Data de criação do token
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Expira em 24h
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Assina com chave HMAC-SHA256
                .compact(); // Gera a string compacta do token
    }

    public String validateTokenAndGetUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Usa a mesma chave para verificar
                .build()
                .parseClaimsJws(token)           // Valida o token
                .getBody()                       // Obtém o corpo do token
                .getSubject();                   // Retorna o nome do usuário
    }
}
package com.nativenews.api.services;

import com.nativenews.api.domain.model.FavoriteModel;
import com.nativenews.api.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    // Método para obter o ID do usuário atualmente autenticado
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    // Criar novo favorito
    public FavoriteModel createFavorite(FavoriteModel favorite) {
        String userId = getCurrentUserId();
        favorite.setUserId(userId);
        favorite.setCreatedAt(LocalDateTime.now());
        return favoriteRepository.save(favorite);
    }

    // Buscar favoritos paginados do usuário atual
    public Page<FavoriteModel> getFavoritesByUser(Pageable pageable) {
        String userId = getCurrentUserId();
        return favoriteRepository.findByUserId(userId, pageable);
    }

    // Atualizar favorito
    public FavoriteModel updateFavorite(Long id, FavoriteModel updatedFavorite) {
        return favoriteRepository.findById(id)
                .map(favorite -> {
                    String userId = getCurrentUserId();

                    // Verificar se o favorito pertence ao usuário atual
                    if (!favorite.getUserId().equals(userId)) {
                        throw new RuntimeException("Não autorizado");
                    }

                    favorite.setTitle(updatedFavorite.getTitle());
                    favorite.setDescription(updatedFavorite.getDescription());
                    favorite.setUrl(updatedFavorite.getUrl());
                    favorite.setBody(updatedFavorite.getBody());
                    favorite.setUpdatedAt(LocalDateTime.now());

                    return favoriteRepository.save(favorite);
                })
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado"));
    }

    // Deletar favorito
    public void deleteFavorite(Long id) {
        FavoriteModel favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado"));

        String userId = getCurrentUserId();
        if (!favorite.getUserId().equals(userId)) {
            throw new RuntimeException("Não autorizado");
        }

        favoriteRepository.delete(favorite);
    }

    // Buscar favorito por ID
    public Optional<FavoriteModel> getFavoriteById(Long id) {
        FavoriteModel favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado"));

        String userId = getCurrentUserId();
        if (!favorite.getUserId().equals(userId)) {
            throw new RuntimeException("Não autorizado");
        }

        return Optional.of(favorite);
    }

    // Buscar favoritos por categoria
    public Page<FavoriteModel> getFavoritesByCategory(String category, Pageable pageable) {
        return favoriteRepository.findByCategory(category, pageable);
    }

    // Buscar favoritos por usuário e categoria
    public Page<FavoriteModel> getFavoritesByUserAndCategory(String userId, String category, Pageable pageable) {
        return favoriteRepository.findByUserIdAndCategory(userId, category, pageable);
    }
}
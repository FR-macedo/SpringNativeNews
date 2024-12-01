package com.nativenews.api.controller;

import com.nativenews.api.domain.model.FavoriteModel;
import com.nativenews.api.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // Criar novo favorito
    @PostMapping
    public ResponseEntity<FavoriteModel> createFavorite(@RequestBody FavoriteModel favorite) {
        FavoriteModel savedFavorite = favoriteService.createFavorite(favorite);
        return ResponseEntity.ok(savedFavorite);
    }

    // Buscar favoritos paginados
    @GetMapping
    public ResponseEntity<Page<FavoriteModel>> getFavorites(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<FavoriteModel> favorites = favoriteService.getFavoritesByUser(pageable);
        return ResponseEntity.ok(favorites);
    }

    // Buscar favorito por ID
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteModel> getFavoriteById(@PathVariable Long id) {
        return favoriteService.getFavoriteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Novo endpoint para buscar favoritos por categoria
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<FavoriteModel>> getFavoritesByCategory(
            @PathVariable String category,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<FavoriteModel> favorites = favoriteService.getFavoritesByCategory(category, pageable);
        return ResponseEntity.ok(favorites);
    }

    // Novo endpoint para buscar favoritos por usuário e categoria
    @GetMapping("/user/{userId}/category/{category}")
    public ResponseEntity<Page<FavoriteModel>> getFavoritesByUserAndCategory(
            @PathVariable String userId,
            @PathVariable String category,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<FavoriteModel> favorites = favoriteService.getFavoritesByUserAndCategory(userId, category, pageable);
        return ResponseEntity.ok(favorites);
    }

    // Atualizar favorito
    @PutMapping("/{id}")
    public ResponseEntity<FavoriteModel> updateFavorite(
            @PathVariable Long id,
            @RequestBody FavoriteModel favorite
    ) {
        FavoriteModel updatedFavorite = favoriteService.updateFavorite(id, favorite);
        return ResponseEntity.ok(updatedFavorite);
    }

    // Deletar favorito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
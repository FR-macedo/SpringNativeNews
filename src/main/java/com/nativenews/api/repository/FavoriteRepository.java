package com.nativenews.api.repository;

import com.nativenews.api.domain.model.FavoriteModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteModel, Long> {
    // Método para buscar favoritos por categoria
    Page<FavoriteModel> findByCategory(String category, Pageable pageable);

    // Método para buscar favoritos por usuário e categoria
    Page<FavoriteModel> findByUserIdAndCategory(String userId, String category, Pageable pageable);

    Page<FavoriteModel> findByUserId(String userId, Pageable pageable);
}
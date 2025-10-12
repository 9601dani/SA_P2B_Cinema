package com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.repository;

import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.entity.CinemaDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface CinemaDbEntityJpaRepository extends JpaRepository<CinemaDbEntity, UUID> {
    Optional<CinemaDbEntity> findByName(String name);
    Optional<CinemaDbEntity> findByAdminUserId(UUID adminUserId);
}

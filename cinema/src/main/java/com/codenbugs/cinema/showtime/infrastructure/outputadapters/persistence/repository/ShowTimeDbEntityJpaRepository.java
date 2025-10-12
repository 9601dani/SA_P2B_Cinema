package com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.repository;

import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.ShowTimeDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ShowTimeDbEntityJpaRepository extends JpaRepository<ShowTimeDbEntity, UUID> {
}

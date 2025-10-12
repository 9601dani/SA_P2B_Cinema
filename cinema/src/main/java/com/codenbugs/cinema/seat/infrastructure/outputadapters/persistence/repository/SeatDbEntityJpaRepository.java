package com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.repository;

import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.SeatDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SeatDbEntityJpaRepository extends JpaRepository<SeatDbEntity, UUID> {
}

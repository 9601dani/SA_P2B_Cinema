package com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.repository;

import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.RoomDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RoomDbEntityJpaRepository extends JpaRepository<RoomDbEntity, UUID> {
}

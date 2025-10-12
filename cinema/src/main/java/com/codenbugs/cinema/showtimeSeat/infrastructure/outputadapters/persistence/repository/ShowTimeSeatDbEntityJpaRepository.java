package com.codenbugs.cinema.showtimeSeat.infrastructure.outputadapters.persistence.repository;

import com.codenbugs.cinema.showtimeSeat.infrastructure.outputadapters.persistence.entity.ShowTimeSeatDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ShowTimeSeatDbEntityJpaRepository extends JpaRepository<ShowTimeSeatDbEntity, UUID> {
}

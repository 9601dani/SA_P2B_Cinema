package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.repository;

import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.TicketSaleDBEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketSaleDBRepository extends JpaRepository<TicketSaleDBEntity, UUID> {

    Optional<TicketSaleDBEntity> findBySeatIdAndShowtimeId(UUID seatId, UUID showtimeId);

    List<TicketSaleDBEntity> findAllByShowtimeId(UUID showtimeId);

    List<TicketSaleDBEntity> findAllByUserId(UUID userId);

    List<TicketSaleDBEntity> findAllByUserIdAndShowtimeId(UUID userId, UUID showtimeId);
}

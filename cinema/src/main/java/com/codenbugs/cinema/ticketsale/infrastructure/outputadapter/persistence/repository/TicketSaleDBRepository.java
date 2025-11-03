package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.repository;

import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.TicketSaleDBEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketSaleDBRepository extends JpaRepository<TicketSaleDBEntity, UUID> {

    Optional<TicketSaleDBEntity> findBySeatIdAndShowtimeId(UUID seatId, UUID showtimeId);
}

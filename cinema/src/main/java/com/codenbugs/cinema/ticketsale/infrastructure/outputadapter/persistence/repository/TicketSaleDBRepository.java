package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.repository;

import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.TicketSaleDBEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketSaleDBRepository extends JpaRepository<TicketSaleDBEntity, UUID> {

    Optional<TicketSaleDBEntity> findBySeatIdAndShowtimeId(UUID seatId, UUID showtimeId);

    List<TicketSaleDBEntity> findAllByShowtimeIdAndState(UUID showtimeId, StateTicket state);

    List<TicketSaleDBEntity> findAllByUserId(UUID userId);

    List<TicketSaleDBEntity> findAllByUserIdAndShowtimeId(UUID userId, UUID showtimeId);

    List<TicketSaleDBEntity> findAllByShowtimeIdInAndState(List<UUID> showtimeIds, StateTicket state);



    @Query("""
    SELECT tk
    FROM ticket_sale tk
    WHERE tk.showtimeId IN :showtimeIds
      AND tk.purchaseDate BETWEEN :startDate AND :endDate
      AND tk.state = :state
""")
    List<TicketSaleDBEntity> findAllByShowtimeIdInAndRangeDateAndState(
            @Param("showtimeIds") List<UUID> showtimeIds,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            @Param("state") StateTicket state
    );
}

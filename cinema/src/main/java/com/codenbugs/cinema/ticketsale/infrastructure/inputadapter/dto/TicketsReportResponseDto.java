package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record TicketsReportResponseDto(
        UUID ticketId,
        UUID showtimeId,
        String nameSeat,
        String userName,
        String movieTitle,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Instant purchaseDate,
        BigDecimal price,
        BigDecimal discountPercentage,
        BigDecimal priceTotal
) {
}

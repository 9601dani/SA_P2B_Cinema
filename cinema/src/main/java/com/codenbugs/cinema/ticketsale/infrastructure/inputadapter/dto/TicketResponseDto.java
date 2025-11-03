package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record TicketResponseDto(
        UUID id,
        UUID showtimeId,
        UUID seatId,
        UUID userId,
        Instant purchaseDate,
        BigDecimal price,
        BigDecimal discountPercentage,
        BigDecimal priceTotal,
        String state,
        UUID promotionId
) {
}

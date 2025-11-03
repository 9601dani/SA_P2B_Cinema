package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto;


import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record PromotionResponseDto(
        UUID id,
        UUID cinemaId,
        String title,
        String description,
        double discountPercentage,
        UUID targetId,
        String targetType,
        boolean isActive,
        String startDate,
        String endDate
) {
}

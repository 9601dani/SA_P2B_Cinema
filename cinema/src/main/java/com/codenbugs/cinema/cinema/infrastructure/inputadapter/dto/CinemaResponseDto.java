package com.codenbugs.cinema.cinema.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(toBuilder = true)
public record CinemaResponseDto (
        UUID id,
        String name,
        String imageUrl,
        String address,
        UUID adminUserId,
        BigDecimal dailyCost
){


}

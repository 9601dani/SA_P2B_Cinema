package com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder(toBuilder = true)
public record ShowTimeResponseDto(
        UUID id,
        UUID roomId,
        BigDecimal price,
        UUID movieId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean active,
        Integer durationMinutes,
        String nameRoom
) {
}

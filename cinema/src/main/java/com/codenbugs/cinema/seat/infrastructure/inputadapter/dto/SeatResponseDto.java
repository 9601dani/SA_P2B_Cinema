package com.codenbugs.cinema.seat.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record SeatResponseDto(
        UUID id,
        UUID roomId,
        String name,
        Integer rowNum,
        Integer colNum
) {
}

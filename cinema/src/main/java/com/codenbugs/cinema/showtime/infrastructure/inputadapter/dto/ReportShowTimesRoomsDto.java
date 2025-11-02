package com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record ReportShowTimesRoomsDto(
        UUID id,
        Integer capacity,
        String imageUrl,
        String name,
        Integer rows,
        Integer columns,
        String description,
        List<ShowTimeResponseDto> showTimes
) {
}

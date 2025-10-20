package com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record MovieResponseDto(
        UUID id,
        String title,
        String posterUrl,
        String synopsis,
        Integer durationMinutes,
        String director,
        String classification,
        LocalDate releaseDate,
        boolean active,
        List<CategoryResponseDto> categories
) {
}


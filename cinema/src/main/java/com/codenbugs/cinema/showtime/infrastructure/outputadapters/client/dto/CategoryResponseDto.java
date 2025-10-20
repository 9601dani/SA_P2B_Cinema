package com.codenbugs.cinema.showtime.infrastructure.outputadapters.client.dto;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CategoryResponseDto(
        UUID id,
        String name
) {
}

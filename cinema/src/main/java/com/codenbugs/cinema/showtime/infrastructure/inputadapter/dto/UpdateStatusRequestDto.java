package com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record UpdateStatusRequestDto(
        boolean active
) {
}

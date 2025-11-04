package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record UserResponseDto(
        UUID id,
        String email,
        String roleName,
        String fullName,
        boolean active
) {
}

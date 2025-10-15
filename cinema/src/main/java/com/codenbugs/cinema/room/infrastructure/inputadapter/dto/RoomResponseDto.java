package com.codenbugs.cinema.room.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record RoomResponseDto(
        UUID id,
        UUID cinemaId,
        Integer capacity,
        String imageUrl,
        String name,
        Integer rows,
        Integer columns,
        String description,
        boolean commentsEnabled,
        boolean blocked
) {
}

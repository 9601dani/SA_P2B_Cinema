package com.codenbugs.cinema.room.infrastructure.inputadapter.dto;

import com.codenbugs.cinema.room.application.usecase.updateroom.UpdateRoomDto;
import jakarta.validation.constraints.NotBlank;

public record UpdateRoomRequestDto(

        @NotBlank
        String name,

        @NotBlank
        String description,

        @NotBlank
        String imageUrl,
        boolean commentsEnabled,
        boolean blocked
) {

    public UpdateRoomDto toDomain(){
        return new UpdateRoomDto(name, description, imageUrl, commentsEnabled, blocked);
    }
}

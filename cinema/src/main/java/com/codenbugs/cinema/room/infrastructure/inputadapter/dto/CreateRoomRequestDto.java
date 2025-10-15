package com.codenbugs.cinema.room.infrastructure.inputadapter.dto;

import com.codenbugs.cinema.room.application.usecase.createroom.CreateRoomDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreateRoomRequestDto(

        @NotNull
        UUID cinemaId,

        @NotBlank
        String name,

        @NotBlank
        String Description,

        @PositiveOrZero
        Integer rows,

        @PositiveOrZero
        Integer columns,

        @NotBlank
        String imageUrl
) {

    public CreateRoomDto toDomain() {
        return new CreateRoomDto(cinemaId, name, Description, rows, columns, imageUrl);
    }
}

package com.codenbugs.cinema.cinema.infrastructure.inputadapter.dto;


import com.codenbugs.cinema.cinema.application.usecase.createcinema.CreateCinemaDto;
import com.codenbugs.cinema.cinema.application.usecase.updatecinema.UpdateCinemaDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record CinemaCreateRequestDto(
        @NotBlank
        String name,

        @NotBlank
        String imageUrl,

        @NotBlank
        String address,

        @NotNull
        UUID adminUserId,

        @PositiveOrZero
        BigDecimal dailyCost
) {
    public CreateCinemaDto toDomain() {
        return new CreateCinemaDto(name, address, adminUserId, dailyCost, imageUrl);
    }

    public UpdateCinemaDto toUpdateDomain(){
        return new UpdateCinemaDto(name, address, adminUserId, dailyCost, imageUrl);
    }
}

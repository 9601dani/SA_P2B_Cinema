package com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto;


import com.codenbugs.cinema.showtime.application.usecase.createshowtime.CreateShowTimeCaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateShowtimeRequestDto(
        @NotNull
        UUID movieId,

        @PositiveOrZero
        BigDecimal price,

        @NotNull
        UUID roomId,

        @NotBlank
        String startDate,

        @NotBlank
        String startTime

) {

    public CreateShowTimeCaseDto toCase(){
        return new CreateShowTimeCaseDto(movieId, price, roomId, LocalDate.parse(startDate), LocalTime.parse(startTime));
    }

}

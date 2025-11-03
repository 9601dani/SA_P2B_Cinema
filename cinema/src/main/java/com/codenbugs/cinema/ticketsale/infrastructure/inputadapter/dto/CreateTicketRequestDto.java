package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto;

import com.codenbugs.cinema.ticketsale.application.usecase.created.CreateTicketSaleCaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CreateTicketRequestDto(
        @NotNull
        UUID walletId,
        @NotNull
        UUID showtimeId,
        @NotNull
        UUID seatId,
        @NotNull
        UUID userId,
        UUID promotionId // si puede ser null
) {

    public CreateTicketSaleCaseDto toCaseDto(){
        return new CreateTicketSaleCaseDto(
                walletId,
                showtimeId,
                seatId,
                userId,
                promotionId
        );
    }
}

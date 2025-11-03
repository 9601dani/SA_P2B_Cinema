package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto;

import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdatingSeatCaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record UpdateSeatRequestDto(
        @NotNull
        UUID id,
        @NotNull
        UUID seatId
) {

    public UpdatingSeatCaseDto toCaseDto() {
        return new UpdatingSeatCaseDto(id, seatId);
    }
}

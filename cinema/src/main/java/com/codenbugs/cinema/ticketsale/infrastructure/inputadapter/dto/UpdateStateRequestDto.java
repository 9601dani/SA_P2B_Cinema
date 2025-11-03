package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto;

import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdateStateCaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record UpdateStateRequestDto(
        @NotNull
        UUID id,
        @NotBlank
        String state,
        @NotNull
        UUID walletId
) {
    public UpdateStateCaseDto toCase(){
        return new  UpdateStateCaseDto(id, state, walletId);
    }
}

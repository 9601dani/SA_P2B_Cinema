package com.codenbugs.cinema.ticketsale.application.usecase.update;

import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class UpdateStateCaseDto {
    UUID id;
    String state;
    UUID walletId;

    public StateTicket convertTargetType() {
        try {
            return StateTicket.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidPropertyEntityDomain(
                    "El tipo de estado '" + state + "' no es válido. Valores permitidos: "
            );
        }
    }
}

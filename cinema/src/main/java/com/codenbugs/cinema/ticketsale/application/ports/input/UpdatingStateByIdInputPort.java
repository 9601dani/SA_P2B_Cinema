package com.codenbugs.cinema.ticketsale.application.ports.input;

import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdateStateCaseDto;

public interface UpdatingStateByIdInputPort {
    void updateStateById(UpdateStateCaseDto dto, boolean createEventPayment);
}

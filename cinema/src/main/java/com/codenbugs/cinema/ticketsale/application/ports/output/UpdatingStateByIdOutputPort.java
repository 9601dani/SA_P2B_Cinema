package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;

import java.util.UUID;

public interface UpdatingStateByIdOutputPort {
    void updateStateById(UUID id, StateTicket state);
}

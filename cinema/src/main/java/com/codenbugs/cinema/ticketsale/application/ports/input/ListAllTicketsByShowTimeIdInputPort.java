package com.codenbugs.cinema.ticketsale.application.ports.input;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.util.List;
import java.util.UUID;

public interface ListAllTicketsByShowTimeIdInputPort {
    List<TicketSaleDomainEntity> listAllTicketsByShowTimeId(UUID cinemaId);
}

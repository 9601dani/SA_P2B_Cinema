package com.codenbugs.cinema.ticketsale.application.usecase.list;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.ticketsale.application.ports.input.ListAllTicketsByShowTimeIdInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketsByShowTimeIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;


@UseCase
@RequiredArgsConstructor
public class ListAllTicketsByShowTimeIdUseCase implements ListAllTicketsByShowTimeIdInputPort {

    private final ListAllTicketsByShowTimeIdOutputPort listAllTicketsByCinemaIdOutputPort;

    @Override
    public List<TicketSaleDomainEntity> listAllTicketsByShowTimeId(UUID cinemaId) {
        return listAllTicketsByCinemaIdOutputPort.listAllTicketsByShowTimeId(cinemaId);
    }
}

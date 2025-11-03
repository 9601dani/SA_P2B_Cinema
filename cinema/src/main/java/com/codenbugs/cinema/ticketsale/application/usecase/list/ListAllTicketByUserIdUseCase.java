package com.codenbugs.cinema.ticketsale.application.usecase.list;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.ticketsale.application.ports.input.ListAllTicketByUserIdInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketByUserIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class ListAllTicketByUserIdUseCase implements ListAllTicketByUserIdInputPort {

    private final ListAllTicketByUserIdOutputPort listAllTicketByUserIdOutputPort;

    @Override
    public List<TicketSaleDomainEntity> listAllTicketsByUserId(UUID userId) {
        return listAllTicketByUserIdOutputPort.listAllTicketsByUserId(userId);
    }
}

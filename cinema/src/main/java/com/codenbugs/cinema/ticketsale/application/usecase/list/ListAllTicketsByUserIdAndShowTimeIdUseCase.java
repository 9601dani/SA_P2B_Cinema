package com.codenbugs.cinema.ticketsale.application.usecase.list;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.ticketsale.application.ports.input.ListAllTicketsByUserIdAndShowTimeIdInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketsByUserIdAndShowTimeIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class ListAllTicketsByUserIdAndShowTimeIdUseCase implements ListAllTicketsByUserIdAndShowTimeIdInputPort {

    private final ListAllTicketsByUserIdAndShowTimeIdOutputPort outputPort;

    @Override
    public List<TicketSaleDomainEntity> listAllTicketsByUserIdAndShoTimeId(UUID userId, UUID showTimeId) {
        return outputPort.listAllTicketsByUserIdAndShowTimeId(userId, showTimeId);
    }
}

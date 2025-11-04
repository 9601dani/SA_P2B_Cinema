package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ListAllTicketsByListShowTimesIdRangDateOutputPort {
    List<TicketSaleDomainEntity> findAllTicketsByListShowTimesIdRangDate(List<UUID> showTimesIds, Instant startDate, Instant endDate);
}

package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence;

import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingTicketByIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingTicketBySeatIdAndShowTimeIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.StoringTicketOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.UpdatingStateByIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.mapper.TicketPersistenceMapper;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.repository.TicketSaleDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements StoringTicketOutputPort, FindingTicketBySeatIdAndShowTimeIdOutputPort,
        UpdatingStateByIdOutputPort, FindingTicketByIdOutputPort {

    private final TicketSaleDBRepository ticketSaleDBRepository;
    private final TicketPersistenceMapper ticketPersistenceMapper;

    @Override
    @Transactional
    public UUID save(TicketSaleDomainEntity domainEntity) {
        var dbEntity = ticketPersistenceMapper.toDbEntity(domainEntity);
        var savedEntity = ticketSaleDBRepository.save(dbEntity);
        return savedEntity.getId();
    }

    @Override
    public Optional<TicketSaleDomainEntity> findBySeatIdAndShowTimeId(UUID seatId, UUID showTimeId) {
        return ticketSaleDBRepository.findBySeatIdAndShowtimeId(seatId, showTimeId)
                .map(ticketPersistenceMapper::toDomainEntity);
    }

    @Override
    @Transactional
    public void updateStateById(UUID id, StateTicket state) {
        var dbEntity = ticketSaleDBRepository.findById(id)
                .orElseThrow(() -> new EntityNotFount("Ticket with id " + id + " not found"));
        dbEntity.setState(state);
        ticketSaleDBRepository.save(dbEntity);

    }

    @Override
    public Optional<TicketSaleDomainEntity> findingTicketById(UUID id) {
        return ticketSaleDBRepository.findById(id)
                .map(ticketPersistenceMapper::toDomainEntity);
    }
}

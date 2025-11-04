package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence;

import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.ticketsale.application.ports.output.*;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.mapper.TicketPersistenceMapper;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.repository.TicketSaleDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements StoringTicketOutputPort, FindingTicketBySeatIdAndShowTimeIdOutputPort,
        UpdatingStateByIdOutputPort, FindingTicketByIdOutputPort, ListAllTicketsByShowTimeIdOutputPort, ListAllTicketByUserIdOutputPort,
        ListAllTicketsByUserIdAndShowTimeIdOutputPort, UpdatingSeatByTicketIdOutputPort, ListAllTicketsByListShowTimesIdOutputPort, ListAllTicketsByListShowTimesIdRangDateOutputPort{

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

    @Override
    public List<TicketSaleDomainEntity> listAllTicketsByShowTimeId(UUID cinemaId) {
        return ticketSaleDBRepository.findAllByShowtimeIdAndState(cinemaId, StateTicket.COMPLETED_PAYMENT).stream()
                .map(ticketPersistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<TicketSaleDomainEntity> listAllTicketsByUserId(UUID userId) {
        return ticketSaleDBRepository.findAllByUserId(userId).stream()
                .map(ticketPersistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<TicketSaleDomainEntity> listAllTicketsByUserIdAndShowTimeId(UUID userId, UUID showTimeId) {
        return ticketSaleDBRepository.findAllByUserIdAndShowtimeId(userId, showTimeId).stream()
                .map(ticketPersistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    @Transactional
    public void updateSeatByTicketIdInDatabase(UUID seatId, UUID ticketId) {
        var ticketCurrent = ticketSaleDBRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFount("Ticket no encontrado para el cambio de asiento"));
        ticketCurrent.setSeatId(seatId);
        ticketSaleDBRepository.save(ticketCurrent);
    }

    @Override
    public List<TicketSaleDomainEntity> findAllTicketsByListShowTimesId(List<UUID> showTimesIds) {
        return ticketSaleDBRepository.findAllByShowtimeIdInAndState(showTimesIds, StateTicket.COMPLETED_PAYMENT).stream()
                .map(ticketPersistenceMapper::toDomainEntity)
                .toList();
    }

    @Override
    public List<TicketSaleDomainEntity> findAllTicketsByListShowTimesIdRangDate(List<UUID> showTimesIds, Instant startDate, Instant endDate) {
        return ticketSaleDBRepository.findAllByShowtimeIdInAndRangeDateAndState(showTimesIds, startDate, endDate, StateTicket.COMPLETED_PAYMENT).stream()
                .map(ticketPersistenceMapper::toDomainEntity)
                .toList();
    }
}

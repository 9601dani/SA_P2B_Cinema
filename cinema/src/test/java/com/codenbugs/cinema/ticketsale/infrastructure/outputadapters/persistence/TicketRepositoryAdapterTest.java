package com.codenbugs.cinema.ticketsale.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.TicketRepositoryAdapter;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.TicketSaleDBEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.mapper.TicketPersistenceMapper;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.repository.TicketSaleDBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketRepositoryAdapterTest {
    private TicketSaleDBRepository repository;
    private TicketPersistenceMapper mapper;
    private TicketRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(TicketSaleDBRepository.class);
        mapper = mock(TicketPersistenceMapper.class);
        adapter = new TicketRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldSaveTicketAndReturnId() {
        // Arrange
        UUID id = UUID.randomUUID();
        TicketSaleDomainEntity domainEntity = new TicketSaleDomainEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.valueOf(50),
                BigDecimal.ZERO,
                StateTicket.PENDING_PAYMENT,
                UUID.randomUUID(),
                null
        );

        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);
        when(mapper.toDbEntity(domainEntity)).thenReturn(dbEntity);
        when(dbEntity.getId()).thenReturn(id);
        when(repository.save(dbEntity)).thenReturn(dbEntity);

        // Act
        UUID result = adapter.save(domainEntity);

        // Assert
        assertEquals(id, result);
        verify(repository, times(1)).save(dbEntity);
    }

    @Test
    void shouldFindBySeatIdAndShowTimeId() {
        // Arrange
        UUID seatId = UUID.randomUUID();
        UUID showtimeId = UUID.randomUUID();
        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);
        TicketSaleDomainEntity domainEntity = mock(TicketSaleDomainEntity.class);

        when(repository.findBySeatIdAndShowtimeId(seatId, showtimeId)).thenReturn(Optional.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domainEntity);

        // Act
        Optional<TicketSaleDomainEntity> result = adapter.findBySeatIdAndShowTimeId(seatId, showtimeId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domainEntity, result.get());
    }

    @Test
    void shouldUpdateStateById() {
        // Arrange
        UUID id = UUID.randomUUID();
        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);

        when(repository.findById(id)).thenReturn(Optional.of(dbEntity));

        // Act
        adapter.updateStateById(id, StateTicket.COMPLETED_PAYMENT);

        // Assert
        verify(dbEntity, times(1)).setState(StateTicket.COMPLETED_PAYMENT);
        verify(repository, times(1)).save(dbEntity);
    }

    @Test
    void shouldThrowWhenUpdatingStateByIdAndNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFount.class, () -> adapter.updateStateById(id, StateTicket.COMPLETED_PAYMENT));
    }

    @Test
    void shouldFindingTicketById() {
        // Arrange
        UUID id = UUID.randomUUID();
        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);
        TicketSaleDomainEntity domainEntity = mock(TicketSaleDomainEntity.class);

        when(repository.findById(id)).thenReturn(Optional.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domainEntity);

        // Act
        Optional<TicketSaleDomainEntity> result = adapter.findingTicketById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domainEntity, result.get());
    }

    @Test
    void shouldUpdateSeatByTicketIdInDatabase() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();
        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);

        when(repository.findById(ticketId)).thenReturn(Optional.of(dbEntity));

        // Act
        adapter.updateSeatByTicketIdInDatabase(seatId, ticketId);

        // Assert
        verify(dbEntity, times(1)).setSeatId(seatId);
        verify(repository, times(1)).save(dbEntity);
    }

    @Test
    void shouldThrowWhenUpdateSeatAndTicketNotFound() {
        UUID ticketId = UUID.randomUUID();
        when(repository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFount.class, () -> adapter.updateSeatByTicketIdInDatabase(UUID.randomUUID(), ticketId));
    }

    @Test
    void shouldListAllTicketsByShowTimeId() {
        // Arrange
        UUID showtimeId = UUID.randomUUID();
        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);
        TicketSaleDomainEntity domainEntity = mock(TicketSaleDomainEntity.class);

        when(repository.findAllByShowtimeIdAndState(showtimeId, StateTicket.COMPLETED_PAYMENT))
                .thenReturn(List.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domainEntity);

        // Act
        List<TicketSaleDomainEntity> result = adapter.listAllTicketsByShowTimeId(showtimeId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(domainEntity, result.get(0));
    }

    @Test
    void shouldListAllTicketsByUserId() {
        // Arrange
        UUID userId = UUID.randomUUID();
        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);
        TicketSaleDomainEntity domainEntity = mock(TicketSaleDomainEntity.class);

        when(repository.findAllByUserId(userId)).thenReturn(List.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domainEntity);

        // Act
        List<TicketSaleDomainEntity> result = adapter.listAllTicketsByUserId(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(domainEntity, result.get(0));
    }

    @Test
    void shouldListAllTicketsByUserIdAndShowTimeId() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID showtimeId = UUID.randomUUID();
        TicketSaleDBEntity dbEntity = mock(TicketSaleDBEntity.class);
        TicketSaleDomainEntity domainEntity = mock(TicketSaleDomainEntity.class);

        when(repository.findAllByUserIdAndShowtimeId(userId, showtimeId)).thenReturn(List.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domainEntity);

        // Act
        List<TicketSaleDomainEntity> result = adapter.listAllTicketsByUserIdAndShowTimeId(userId, showtimeId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(domainEntity, result.get(0));
    }
}

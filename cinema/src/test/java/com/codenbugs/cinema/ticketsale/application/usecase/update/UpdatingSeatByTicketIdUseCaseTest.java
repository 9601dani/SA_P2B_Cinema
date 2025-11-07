package com.codenbugs.cinema.ticketsale.application.usecase.update;

import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.seat.application.ports.output.FindingSeatByIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingTicketByIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingTicketBySeatIdAndShowTimeIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.UpdatingSeatByTicketIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdatingSeatByTicketIdUseCaseTest {
    private UpdatingSeatByTicketIdOutputPort updatingSeatPort;
    private FindingSeatByIdOutputPort seatPort;
    private FindingTicketByIdOutputPort ticketPort;
    private FindingTicketBySeatIdAndShowTimeIdOutputPort seatTicketPort;

    private UpdatingSeatByTicketIdUseCase useCase;

    @BeforeEach
    void setUp() {
        updatingSeatPort = mock(UpdatingSeatByTicketIdOutputPort.class);
        seatPort = mock(FindingSeatByIdOutputPort.class);
        ticketPort = mock(FindingTicketByIdOutputPort.class);
        seatTicketPort = mock(FindingTicketBySeatIdAndShowTimeIdOutputPort.class);

        useCase = new UpdatingSeatByTicketIdUseCase(
                updatingSeatPort, seatPort, ticketPort, seatTicketPort
        );
    }

    @Test
    void shouldUpdateSeatSuccessfully() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID showTimeId = UUID.randomUUID();
        UUID oldSeatId = UUID.randomUUID();
        UUID newSeatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();

        TicketSaleDomainEntity ticket = new TicketSaleDomainEntity(
                showTimeId, oldSeatId, userId, walletId, null
        );

        SeatDomainEntity seat = new SeatDomainEntity(1, 1, "A-01", UUID.randomUUID());

        UpdatingSeatCaseDto command = new UpdatingSeatCaseDto(ticketId, newSeatId);

        when(ticketPort.findingTicketById(ticketId)).thenReturn(Optional.of(ticket));
        when(seatPort.findById(newSeatId)).thenReturn(Optional.of(seat));
        when(seatTicketPort.findBySeatIdAndShowTimeId(newSeatId, showTimeId)).thenReturn(Optional.empty());

        // Act
        useCase.updateSeatByTicketId(command, ticketId);

        // Assert
        verify(updatingSeatPort, times(1)).updateSeatByTicketIdInDatabase(newSeatId, ticketId);
    }

    @Test
    void shouldThrowWhenTicketNotFound() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID newSeatId = UUID.randomUUID();
        UpdatingSeatCaseDto command = new UpdatingSeatCaseDto(ticketId, newSeatId);

        when(ticketPort.findingTicketById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount ex = assertThrows(EntityNotFount.class, () ->
                useCase.updateSeatByTicketId(command, ticketId)
        );

        assertEquals("Ticket no encontrado para actualizar el asieto.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenSeatNotFound() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID oldSeatId = UUID.randomUUID();
        UUID showTimeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        UUID newSeatId = UUID.randomUUID();

        TicketSaleDomainEntity ticket = new TicketSaleDomainEntity(
                showTimeId, oldSeatId, userId, walletId, null
        );

        UpdatingSeatCaseDto command = new UpdatingSeatCaseDto(ticketId, newSeatId);

        when(ticketPort.findingTicketById(ticketId)).thenReturn(Optional.of(ticket));
        when(seatPort.findById(newSeatId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount ex = assertThrows(EntityNotFount.class, () ->
                useCase.updateSeatByTicketId(command, ticketId)
        );

        assertEquals("Asiento no encontrado para la actualizacion de ticket.", ex.getMessage());
    }

}

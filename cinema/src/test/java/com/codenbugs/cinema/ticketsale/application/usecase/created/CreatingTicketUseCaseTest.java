package com.codenbugs.cinema.ticketsale.application.usecase.created;

import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.common.infrastructure.exception.BadRequestException;
import com.codenbugs.cinema.seat.application.ports.output.FindingSeatByIdOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.output.FindingShowTimeByIdOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.ticketsale.application.ports.output.*;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CreatingTicketUseCaseTest {
    @Mock
    private StoringTicketOutputPort storingTicketOutputPort;
    @Mock
    private FindingShowTimeByIdOutputPort findingShowTimeByIdOutputPort;
    @Mock
    private FindingSeatByIdOutputPort findingSeatByIdOutputPort;
    @Mock
    private FindingPromotionByIdOutputPort findingPromotionByIdOutputPort;
    @Mock
    private FindingTicketBySeatIdAndShowTimeIdOutputPort findingTicketBySeatIdAndShowTimeIdOutputPort;
    @Mock
    private PayTicketEventPort payTicketEventPort;
    @Mock
    private UpdatingStateByIdOutputPort updatingStateByIdOutputPort;

    private CreatingTicketUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreatingTicketUseCase(
                storingTicketOutputPort,
                findingShowTimeByIdOutputPort,
                findingSeatByIdOutputPort,
                findingPromotionByIdOutputPort,
                findingTicketBySeatIdAndShowTimeIdOutputPort,
                payTicketEventPort,
                updatingStateByIdOutputPort
        );
    }

    private ShowTimeDomainEntity createValidShowTime() {
        UUID id = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        BigDecimal price = BigDecimal.valueOf(100);
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = startTime.plusHours(2);
        return new ShowTimeDomainEntity(id, roomId, price, movieId, startTime, endTime, true);
    }

    private SeatDomainEntity createValidSeat() {
        return new SeatDomainEntity(UUID.randomUUID(), 1, 1, "A1", UUID.randomUUID());
    }

    @Test
    void shouldCreateTicketSuccessfullyWithoutPromotion() {
        // Arrange
        ShowTimeDomainEntity showTime = createValidShowTime();
        SeatDomainEntity seat = createValidSeat();
        UUID walletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CreateTicketSaleCaseDto dto = new CreateTicketSaleCaseDto(walletId, showTime.getId(), seat.getId(), userId, null);

        when(findingShowTimeByIdOutputPort.findById(showTime.getId())).thenReturn(Optional.of(showTime));
        when(findingSeatByIdOutputPort.findById(seat.getId())).thenReturn(Optional.of(seat));
        when(findingTicketBySeatIdAndShowTimeIdOutputPort.findBySeatIdAndShowTimeId(seat.getId(), showTime.getId()))
                .thenReturn(Optional.empty());
        when(storingTicketOutputPort.save(any())).thenReturn(UUID.randomUUID());

        // Act & Assert
        assertDoesNotThrow(() -> useCase.createTicket(dto));

        verify(storingTicketOutputPort, times(1)).save(any());
        verify(payTicketEventPort, times(2)).payTicketEvent(any(), any());
    }

    @Test
    void shouldThrowEntityNotFoundIfShowTimeDoesNotExist() {
        UUID showTimeId = UUID.randomUUID();
        CreateTicketSaleCaseDto dto = new CreateTicketSaleCaseDto(UUID.randomUUID(), showTimeId, UUID.randomUUID(), UUID.randomUUID(), null);

        when(findingShowTimeByIdOutputPort.findById(showTimeId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFount.class, () -> useCase.createTicket(dto));
    }

    @Test
    void shouldThrowEntityNotFoundIfSeatDoesNotExist() {
        ShowTimeDomainEntity showTime = createValidShowTime();
        UUID seatId = UUID.randomUUID();
        CreateTicketSaleCaseDto dto = new CreateTicketSaleCaseDto(UUID.randomUUID(), showTime.getId(), seatId, UUID.randomUUID(), null);

        when(findingShowTimeByIdOutputPort.findById(showTime.getId())).thenReturn(Optional.of(showTime));
        when(findingSeatByIdOutputPort.findById(seatId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFount.class, () -> useCase.createTicket(dto));
    }

    @Test
    void shouldThrowEntityAlreadyExistsIfSeatOccupied() {
        ShowTimeDomainEntity showTime = createValidShowTime();
        SeatDomainEntity seat = createValidSeat();
        TicketSaleDomainEntity existingTicket = new TicketSaleDomainEntity(
                showTime.getId(), seat.getId(), UUID.randomUUID(), BigDecimal.valueOf(100),
                BigDecimal.ZERO, StateTicket.COMPLETED_PAYMENT, UUID.randomUUID(), null
        );

        CreateTicketSaleCaseDto dto = new CreateTicketSaleCaseDto(UUID.randomUUID(), showTime.getId(), seat.getId(), UUID.randomUUID(), null);

        when(findingShowTimeByIdOutputPort.findById(showTime.getId())).thenReturn(Optional.of(showTime));
        when(findingSeatByIdOutputPort.findById(seat.getId())).thenReturn(Optional.of(seat));
        when(findingTicketBySeatIdAndShowTimeIdOutputPort.findBySeatIdAndShowTimeId(seat.getId(), showTime.getId()))
                .thenReturn(Optional.of(existingTicket));

        assertThrows(EntityAlreadyExistsException.class, () -> useCase.createTicket(dto));
    }

    @Test
    void shouldProcessPaymentExceptionAndRejectTicket() {
        ShowTimeDomainEntity showTime = createValidShowTime();
        SeatDomainEntity seat = createValidSeat();
        UUID walletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        CreateTicketSaleCaseDto dto = new CreateTicketSaleCaseDto(walletId, showTime.getId(), seat.getId(), userId, null);

        when(findingShowTimeByIdOutputPort.findById(showTime.getId())).thenReturn(Optional.of(showTime));
        when(findingSeatByIdOutputPort.findById(seat.getId())).thenReturn(Optional.of(seat));
        when(findingTicketBySeatIdAndShowTimeIdOutputPort.findBySeatIdAndShowTimeId(seat.getId(), showTime.getId()))
                .thenReturn(Optional.empty());
        UUID ticketId = UUID.randomUUID();
        when(storingTicketOutputPort.save(any())).thenReturn(ticketId);

        doNothing().doThrow(new RuntimeException("Payment failed"))
                .when(payTicketEventPort).payTicketEvent(any(), any());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> useCase.createTicket(dto));

        assertEquals("Error al procesar el pago del ticket", ex.getMessage());
        verify(updatingStateByIdOutputPort, times(1)).updateStateById(ticketId, StateTicket.REJECTED);
    }
}

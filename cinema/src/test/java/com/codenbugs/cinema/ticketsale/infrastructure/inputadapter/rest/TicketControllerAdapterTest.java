package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.showtime.application.usecase.reporting.ReportingRangeDto;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.ReportingRangeRequestDto;
import com.codenbugs.cinema.ticketsale.application.ports.input.*;
import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdateStateCaseDto;
import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdatingSeatCaseDto;
import com.codenbugs.cinema.ticketsale.domain.model.ReportTicketsPerRoomEntityDomain;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.*;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.mapper.TicketRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketControllerAdapterTest {
    private CreatingTicketInputPort creatingTicketInputPort;
    private UpdatingStateByIdInputPort updatingStateByIdInputPort;
    private ListAllTicketByUserIdInputPort listAllTicketByUserIdInputPort;
    private ListAllTicketsByShowTimeIdInputPort listAllTicketsByShowTimeIdInputPort;
    private ListAllTicketsByUserIdAndShowTimeIdInputPort listAllTicketsByUserIdAndShowTimeIdInputPort;
    private UpdatingSeatByTicketIdInputPort updatingSeatByTicketIdInputPort;
    private ReportingTicketsSaleByCinemaIdInputPort reportingTicketsSaleByCinemaIdInputPort;
    private ReportingTicketsSaleByCinemaIdRangeDateInputPort reportingTicketsSaleByCinemaIdRangeDateInputPort;
    private TicketRestMapper ticketRestMapper;

    private TicketControllerAdapter controller;

    @BeforeEach
    void setUp() {
        creatingTicketInputPort = mock(CreatingTicketInputPort.class);
        updatingStateByIdInputPort = mock(UpdatingStateByIdInputPort.class);
        listAllTicketByUserIdInputPort = mock(ListAllTicketByUserIdInputPort.class);
        listAllTicketsByShowTimeIdInputPort = mock(ListAllTicketsByShowTimeIdInputPort.class);
        listAllTicketsByUserIdAndShowTimeIdInputPort = mock(ListAllTicketsByUserIdAndShowTimeIdInputPort.class);
        updatingSeatByTicketIdInputPort = mock(UpdatingSeatByTicketIdInputPort.class);
        reportingTicketsSaleByCinemaIdInputPort = mock(ReportingTicketsSaleByCinemaIdInputPort.class);
        reportingTicketsSaleByCinemaIdRangeDateInputPort = mock(ReportingTicketsSaleByCinemaIdRangeDateInputPort.class);
        ticketRestMapper = mock(TicketRestMapper.class);

        controller = new TicketControllerAdapter(
                creatingTicketInputPort,
                updatingStateByIdInputPort,
                listAllTicketByUserIdInputPort,
                listAllTicketsByShowTimeIdInputPort,
                listAllTicketsByUserIdAndShowTimeIdInputPort,
                updatingSeatByTicketIdInputPort,
                reportingTicketsSaleByCinemaIdInputPort,
                reportingTicketsSaleByCinemaIdRangeDateInputPort,
                ticketRestMapper
        );
    }

    @Test
    void shouldCallCreateTicketAndReturnCreated() {
        // Arrange
        UUID walletId = UUID.randomUUID();
        UUID showtimeId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreateTicketRequestDto dto = CreateTicketRequestDto.builder()
                .walletId(walletId)
                .showtimeId(showtimeId)
                .seatId(seatId)
                .userId(userId)
                .promotionId(null)
                .build();

        // Act
        ResponseEntity<Void> response = controller.createTicket(dto);

        // Assert
        verify(creatingTicketInputPort, times(1)).createTicket(dto.toCaseDto());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldCallUpdateStateByIdAndReturnNoContent() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        UpdateStateRequestDto requestDto = UpdateStateRequestDto.builder()
                .id(ticketId)
                .state("COMPLETED")
                .walletId(walletId)
                .build();

        // Act
        ResponseEntity<Void> response = controller.updateStateById(requestDto, ticketId);

        // Assert
        verify(updatingStateByIdInputPort, times(1)).updateStateById(requestDto.toCase(), true);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldListAllTicketsByUserId() {
        // Arrange
        UUID userId = UUID.randomUUID();
        TicketSaleDomainEntity ticketEntity = new TicketSaleDomainEntity(
                UUID.randomUUID(), // showtimeId
                UUID.randomUUID(), // seatId
                userId,
                UUID.randomUUID(), // walletId
                null               // promotionId
        );
        TicketResponseDto ticketDto = new TicketResponseDto(
                UUID.randomUUID(),
                ticketEntity.getShowtimeId(),
                ticketEntity.getSeatId(),
                ticketEntity.getUserId(),
                Instant.now(),
                BigDecimal.TEN,
                BigDecimal.ZERO,
                BigDecimal.TEN,
                "PENDING_PAYMENT",
                null
        );

        when(listAllTicketByUserIdInputPort.listAllTicketsByUserId(userId))
                .thenReturn(List.of(ticketEntity)); // devuelve entidad
        when(ticketRestMapper.toResponseDto(ticketEntity))
                .thenReturn(ticketDto); // mapper convierte a DTO

        // Act
        ResponseEntity<List<TicketResponseDto>> response = controller.listAllTicketByUserId(userId);

        // Assert
        assertEquals(1, response.getBody().size());
        assertEquals(ticketDto, response.getBody().get(0));
    }

    @Test
    void shouldUpdateSeatByTicketIdAndReturnNoContent() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();
        UpdateSeatRequestDto requestDto = UpdateSeatRequestDto.builder()
                .id(ticketId)
                .seatId(seatId)
                .build();

        // Act
        ResponseEntity<Void> response = controller.updateSeatByTicketId(requestDto, ticketId);

        // Assert
        verify(updatingSeatByTicketIdInputPort, times(1))
                .updateSeatByTicketId(requestDto.toCaseDto(), ticketId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldReturnReportTicketsSaleByCinemaId() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        var reportEntity = mock(com.codenbugs.cinema.ticketsale.domain.model.ReportTicketsPerRoomEntityDomain.class);
        ReportTicketsPerRoomResponseDto reportDto = ReportTicketsPerRoomResponseDto.builder()
                .roomId(UUID.randomUUID())
                .capacity(100)
                .imageUrl("img")
                .name("Room 1")
                .rows(10)
                .columns(10)
                .ticketsSold(List.of())
                .build();

        when(reportingTicketsSaleByCinemaIdInputPort.reportTicketsSaleByCinemaId(cinemaId))
                .thenReturn(List.of(reportEntity));
        when(ticketRestMapper.toReportResponseDto(reportEntity))
                .thenReturn(reportDto);

        // Act
        ResponseEntity<List<ReportTicketsPerRoomResponseDto>> response = controller.reportTicketsSaleByCinemaId(cinemaId);

        // Assert
        assertEquals(1, response.getBody().size());
        assertEquals(reportDto, response.getBody().get(0));
    }
}

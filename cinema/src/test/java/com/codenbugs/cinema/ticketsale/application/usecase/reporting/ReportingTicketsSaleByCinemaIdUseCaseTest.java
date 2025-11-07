package com.codenbugs.cinema.ticketsale.application.usecase.reporting;

import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.seat.application.ports.output.FindingAllSeatsByListRoomIdsOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdOutputPort;
import com.codenbugs.cinema.showtime.domain.model.CategoryDomainEntity;
import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingAllCustomersOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingAllMoviesOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketsByListShowTimesIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.CustomerDomainEntity;
import com.codenbugs.cinema.ticketsale.domain.model.ReportTicketsPerRoomEntityDomain;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.domain.model.TicketsReportDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportingTicketsSaleByCinemaIdUseCaseTest {
    private FindingAllRoomsByCinemaIdOutputPort roomsPort;
    private ListAllShowTimesByListRoomsIdOutputPort showTimesPort;
    private ListAllTicketsByListShowTimesIdOutputPort ticketsPort;
    private FindingAllMoviesOutputPort moviesPort;
    private FindingAllCustomersOutputPort customersPort;
    private FindingAllSeatsByListRoomIdsOutputPort seatsPort;

    private ReportingTicketsSaleByCinemaIdUseCase useCase;

    @BeforeEach
    void setUp() {
        roomsPort = mock(FindingAllRoomsByCinemaIdOutputPort.class);
        showTimesPort = mock(ListAllShowTimesByListRoomsIdOutputPort.class);
        ticketsPort = mock(ListAllTicketsByListShowTimesIdOutputPort.class);
        moviesPort = mock(FindingAllMoviesOutputPort.class);
        customersPort = mock(FindingAllCustomersOutputPort.class);
        seatsPort = mock(FindingAllSeatsByListRoomIdsOutputPort.class);

        useCase = new ReportingTicketsSaleByCinemaIdUseCase(
                roomsPort, showTimesPort, ticketsPort, moviesPort, customersPort, seatsPort
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoRoomsFound() {
        UUID cinemaId = UUID.randomUUID();
        when(roomsPort.findAllByCinemaId(cinemaId)).thenReturn(List.of());

        List<ReportTicketsPerRoomEntityDomain> result = useCase.reportTicketsSaleByCinemaId(cinemaId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roomsPort, times(1)).findAllByCinemaId(cinemaId);
    }

    @Test
    void shouldReturnEmptyListWhenNoShowTimesFound() {
        UUID cinemaId = UUID.randomUUID();

        RoomDomainEntity room = new RoomDomainEntity(
                cinemaId,
                "image-url",
                "Sala 1",
                5,
                5,
                "Sala de prueba con suficiente descripción"
        );
        when(roomsPort.findAllByCinemaId(cinemaId)).thenReturn(List.of(room));
        when(showTimesPort.findAllShowTimesByListRoomsId(anyList())).thenReturn(List.of());

        List<ReportTicketsPerRoomEntityDomain> result = useCase.reportTicketsSaleByCinemaId(cinemaId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(showTimesPort, times(1)).findAllShowTimesByListRoomsId(anyList());
    }

}

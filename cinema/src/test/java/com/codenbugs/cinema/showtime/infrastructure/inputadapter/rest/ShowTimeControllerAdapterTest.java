package com.codenbugs.cinema.showtime.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.input.*;
import com.codenbugs.cinema.showtime.application.usecase.createshowtime.CreateShowTimeCaseDto;
import com.codenbugs.cinema.showtime.application.usecase.reporting.ReportingRangeDto;
import com.codenbugs.cinema.showtime.application.usecase.updateactive.UpdateActiveCase;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.*;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.mapper.ShowTimeRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShowTimeControllerAdapterTest {
    @Mock private CreatingShowTimeInputPort creatingShowTimeInputPort;
    @Mock private ListAllShowTimesByCinemaIdInputPort listAllShowTimesByCinemaIdInputPort;
    @Mock private ShowTimeRestMapper mapper;
    @Mock private UpdatingShowTimeActiveByIdInputPort updatingShowTimeActiveByIdInputPort;
    @Mock private ReportingShowTimesPerRoomByCinemaIdInputPort reportingShowTimesPerRoomByCinemaIdInputPort;
    @Mock private ReportingShowTimesPerRoomByCinemaIdRangeDateInputPort reportingShowTimesPerRoomByCinemaIdRangeDateInputPort;

    @InjectMocks private ShowTimeControllerAdapter controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateShowTimeSuccessfully() {
        // Arrange
        UUID movieId = UUID.randomUUID();
        UUID roomId = UUID.randomUUID();
        CreateShowtimeRequestDto requestDto = new CreateShowtimeRequestDto(
                movieId,
                BigDecimal.valueOf(50),
                roomId,
                LocalDate.now().toString(),
                LocalTime.now().toString()
        );

        // Act
        ResponseEntity<Void> response = controller.createShowTime(requestDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(creatingShowTimeInputPort, times(1)).creatShowTime(any(CreateShowTimeCaseDto.class));
    }

    @Test
    void shouldListAllShowTimesByCinemaId() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        ShowTimeDomainEntity showTimeDomain = mock(ShowTimeDomainEntity.class);
        ShowTimeResponseDto responseDto = ShowTimeResponseDto.builder()
                .id(UUID.randomUUID())
                .roomId(UUID.randomUUID())
                .price(BigDecimal.valueOf(40))
                .movieId(UUID.randomUUID())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(2))
                .active(true)
                .durationMinutes(120)
                .nameRoom("Sala 1")
                .build();

        when(listAllShowTimesByCinemaIdInputPort.listAllShowTimesByCinemaId(cinemaId))
                .thenReturn(List.of(showTimeDomain));  // <-- aquí NO vacía
        when(mapper.toResponseDto(showTimeDomain)).thenReturn(responseDto);

        // Act
        ResponseEntity<List<ShowTimeResponseDto>> response = controller.listAllShowTimesByCinemaId(cinemaId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertSame(responseDto, response.getBody().get(0));
        verify(listAllShowTimesByCinemaIdInputPort, times(1)).listAllShowTimesByCinemaId(cinemaId);
    }

    @Test
    void shouldUpdateShowtimeStatus() {
        // Arrange
        UUID showtimeId = UUID.randomUUID();
        UpdateStatusRequestDto requestDto = new UpdateStatusRequestDto(true);

        // Act
        ResponseEntity<Void> response = controller.updateShowtimeStatus(showtimeId, requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updatingShowTimeActiveByIdInputPort, times(1))
                .updateActive(new UpdateActiveCase(requestDto.active(), showtimeId));
    }

    @Test
    void shouldReturnReportByRangeDate() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        ReportingRangeRequestDto requestDto = ReportingRangeRequestDto.builder()
                .targetId(cinemaId)
                .startDate(LocalDate.now().toString())
                .endDate(LocalDate.now().plusDays(1).toString())
                .build();

        RoomDomainEntity room = mock(RoomDomainEntity.class);
        ReportShowTimesRoomsDto reportDto = ReportShowTimesRoomsDto.builder()
                .id(UUID.randomUUID())
                .name("Sala 1")
                .capacity(100)
                .imageUrl("url")
                .rows(10)
                .columns(10)
                .description("Descripcion valida")
                .showTimes(List.of())
                .build();

        when(reportingShowTimesPerRoomByCinemaIdRangeDateInputPort
                .reportShowTimesPerRoomByCinemaIdRangeDate(any(ReportingRangeDto.class)))
                .thenReturn(List.of(room));  // <-- NO vacía
        when(mapper.toReportResponseDto(room)).thenReturn(reportDto);

        // Act
        ResponseEntity<List<ReportShowTimesRoomsDto>> response =
                controller.reportingShowTimesPerRoomByCinemaIdRangeDate(requestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertSame(reportDto, response.getBody().get(0));
    }

    @Test
    void shouldReturnReportByCinemaId() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        RoomDomainEntity room = mock(RoomDomainEntity.class);
        ReportShowTimesRoomsDto reportDto = ReportShowTimesRoomsDto.builder()
                .id(UUID.randomUUID())
                .name("Sala 1")
                .capacity(100)
                .imageUrl("url")
                .rows(10)
                .columns(10)
                .description("Descripcion valida")
                .showTimes(List.of())
                .build();

        when(reportingShowTimesPerRoomByCinemaIdInputPort
                .reportShowTimesPerRoomByCinemaId(cinemaId))
                .thenReturn(List.of(room));

        when(mapper.toReportResponseDto(room)).thenReturn(reportDto);

        // Act
        ResponseEntity<List<ReportShowTimesRoomsDto>> response =
                controller.reportingShowTimesPerRoomByCinemaId(cinemaId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertSame(reportDto, response.getBody().get(0));

        verify(reportingShowTimesPerRoomByCinemaIdInputPort, times(1))
                .reportShowTimesPerRoomByCinemaId(cinemaId);
        verify(mapper, times(1)).toReportResponseDto(room);
    }
}

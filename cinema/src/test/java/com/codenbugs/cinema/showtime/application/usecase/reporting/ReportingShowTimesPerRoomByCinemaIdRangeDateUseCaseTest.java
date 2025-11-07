package com.codenbugs.cinema.showtime.application.usecase.reporting;

import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdRangeDateOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportingShowTimesPerRoomByCinemaIdRangeDateUseCaseTest {
    private FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;
    private ListAllShowTimesByListRoomsIdRangeDateOutputPort listAllShowTimesByListRoomsIdRangeDateOutputPort;
    private ReportingShowTimesPerRoomByCinemaIdRangeDateUseCase useCase;

    @BeforeEach
    void setUp() {
        findingAllRoomsByCinemaIdOutputPort = mock(FindingAllRoomsByCinemaIdOutputPort.class);
        listAllShowTimesByListRoomsIdRangeDateOutputPort = mock(ListAllShowTimesByListRoomsIdRangeDateOutputPort.class);
        useCase = new ReportingShowTimesPerRoomByCinemaIdRangeDateUseCase(
                findingAllRoomsByCinemaIdOutputPort,
                listAllShowTimesByListRoomsIdRangeDateOutputPort
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoRooms() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        ReportingRangeDto dto = new ReportingRangeDto(cinemaId, LocalDate.now(), LocalDate.now().plusDays(1));
        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId)).thenReturn(List.of());

        // Act
        List<RoomDomainEntity> result = useCase.reportShowTimesPerRoomByCinemaIdRangeDate(dto);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(listAllShowTimesByListRoomsIdRangeDateOutputPort, never()).findAllShowTimesByListRoomsIdRangeDate(any(), any(), any());
    }

    @Test
    void shouldReturnRoomsWithEmptyShowTimesWhenNoShowTimes() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        RoomDomainEntity room = new RoomDomainEntity(
                UUID.randomUUID(), cinemaId, 100,
                "url", "Sala 1", 10, 10,
                "Descripcion sala valida", true, false
        );
        ReportingRangeDto dto = new ReportingRangeDto(cinemaId, LocalDate.now(), LocalDate.now().plusDays(1));

        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId))
                .thenReturn(List.of(room));
        when(listAllShowTimesByListRoomsIdRangeDateOutputPort.findAllShowTimesByListRoomsIdRangeDate(
                anyList(), any(), any()))
                .thenReturn(List.of());

        // Act
        List<RoomDomainEntity> result = useCase.reportShowTimesPerRoomByCinemaIdRangeDate(dto);

        // Assert
        assertEquals(1, result.size());
        assertEquals(room.getId(), result.get(0).getId());
        assertNotNull(result.get(0).getShowTimes());
        assertTrue(result.get(0).getShowTimes().isEmpty());
    }

    @Test
    void shouldReturnRoomsWithShowTimesGroupedByRoom() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        UUID roomId1 = UUID.randomUUID();
        UUID roomId2 = UUID.randomUUID();

        RoomDomainEntity room1 = new RoomDomainEntity(
                roomId1, cinemaId, 100, "url", "Sala 1", 10, 10, "Descripcion sala valida", true, false
        );
        RoomDomainEntity room2 = new RoomDomainEntity(
                roomId2, cinemaId, 120, "url2", "Sala 2", 12, 10, "Otra descripcion valida", true, false
        );

        ShowTimeDomainEntity showTime1 = new ShowTimeDomainEntity(
                roomId1, BigDecimal.valueOf(10), UUID.randomUUID(), LocalDateTime.now()
        );
        ShowTimeDomainEntity showTime2 = new ShowTimeDomainEntity(
                roomId1, BigDecimal.valueOf(10), UUID.randomUUID(), LocalDateTime.now().plusHours(1)
        );
        ShowTimeDomainEntity showTime3 = new ShowTimeDomainEntity(
                roomId2, BigDecimal.valueOf(10), UUID.randomUUID(), LocalDateTime.now()
        );

        ReportingRangeDto dto = new ReportingRangeDto(cinemaId, LocalDate.now(), LocalDate.now().plusDays(1));

        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId))
                .thenReturn(List.of(room1, room2));
        when(listAllShowTimesByListRoomsIdRangeDateOutputPort.findAllShowTimesByListRoomsIdRangeDate(
                anyList(), any(), any()))
                .thenReturn(List.of(showTime1, showTime2, showTime3));

        // Act
        List<RoomDomainEntity> result = useCase.reportShowTimesPerRoomByCinemaIdRangeDate(dto);

        // Assert
        assertEquals(2, result.size());

        RoomDomainEntity resultRoom1 = result.stream().filter(r -> r.getId().equals(roomId1)).findFirst().get();
        RoomDomainEntity resultRoom2 = result.stream().filter(r -> r.getId().equals(roomId2)).findFirst().get();

        assertEquals(2, resultRoom1.getShowTimes().size());
        assertTrue(resultRoom1.getShowTimes().contains(showTime1));
        assertTrue(resultRoom1.getShowTimes().contains(showTime2));

        assertEquals(1, resultRoom2.getShowTimes().size());
        assertTrue(resultRoom2.getShowTimes().contains(showTime3));
    }

    @Test
    void shouldCallOutputPortWithCorrectParameters() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        RoomDomainEntity room = new RoomDomainEntity(
                UUID.randomUUID(), cinemaId, 100, "url", "Sala 1", 10, 10, "Descripcion sala valida", true, false
        );
        ReportingRangeDto dto = new ReportingRangeDto(cinemaId, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 2));

        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId)).thenReturn(List.of(room));
        when(listAllShowTimesByListRoomsIdRangeDateOutputPort.findAllShowTimesByListRoomsIdRangeDate(anyList(), any(), any()))
                .thenReturn(List.of());

        // Act
        useCase.reportShowTimesPerRoomByCinemaIdRangeDate(dto);

        // Assert
        ArgumentCaptor<List<UUID>> roomsCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<LocalDateTime> startCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> endCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(listAllShowTimesByListRoomsIdRangeDateOutputPort)
                .findAllShowTimesByListRoomsIdRangeDate(roomsCaptor.capture(), startCaptor.capture(), endCaptor.capture());

        assertEquals(1, roomsCaptor.getValue().size());
        assertEquals(room.getId(), roomsCaptor.getValue().get(0));
        assertEquals(dto.getStartDate().atStartOfDay(), startCaptor.getValue());
        assertEquals(dto.getEndDate().atStartOfDay(), endCaptor.getValue());
    }
}

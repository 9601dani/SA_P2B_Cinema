package com.codenbugs.cinema.showtime.application.usecase.listallshowbycinema;

import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllShowTimeByCinemaIdUseCaseTest {
    private FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;
    private ListAllShowTimesByListRoomsIdOutputPort listAllShowTimesByListRoomsIdOutputPort;
    private ListAllShowTimesByCinemaIdUseCase useCase;

    @BeforeEach
    void setUp() {
        findingAllRoomsByCinemaIdOutputPort = mock(FindingAllRoomsByCinemaIdOutputPort.class);
        listAllShowTimesByListRoomsIdOutputPort = mock(ListAllShowTimesByListRoomsIdOutputPort.class);
        useCase = new ListAllShowTimesByCinemaIdUseCase(
                findingAllRoomsByCinemaIdOutputPort,
                listAllShowTimesByListRoomsIdOutputPort
        );
    }

    @Test
    void shouldReturnShowTimesWithRoomNames() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        UUID roomId1 = UUID.randomUUID();
        UUID roomId2 = UUID.randomUUID();

        RoomDomainEntity room1 = mock(RoomDomainEntity.class);
        when(room1.getId()).thenReturn(roomId1);
        when(room1.getName()).thenReturn("Sala 1");

        RoomDomainEntity room2 = mock(RoomDomainEntity.class);
        when(room2.getId()).thenReturn(roomId2);
        when(room2.getName()).thenReturn("Sala 2");

        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId))
                .thenReturn(Arrays.asList(room1, room2));

        ShowTimeDomainEntity st1 = new ShowTimeDomainEntity(roomId1, BigDecimal.TEN, UUID.randomUUID(),
                LocalDate.now().atTime(LocalTime.of(10, 0)));
        ShowTimeDomainEntity st2 = new ShowTimeDomainEntity(roomId2, BigDecimal.TEN, UUID.randomUUID(),
                LocalDate.now().atTime(LocalTime.of(12, 0)));

        when(listAllShowTimesByListRoomsIdOutputPort.findAllShowTimesByListRoomsId(anyList()))
                .thenReturn(Arrays.asList(st1, st2));

        // Act
        List<ShowTimeDomainEntity> result = useCase.listAllShowTimesByCinemaId(cinemaId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Sala 1", result.get(0).getNameRoom());
        assertEquals("Sala 2", result.get(1).getNameRoom());

        verify(findingAllRoomsByCinemaIdOutputPort, times(1)).findAllByCinemaId(cinemaId);
        verify(listAllShowTimesByListRoomsIdOutputPort, times(1)).findAllShowTimesByListRoomsId(anyList());
    }

    @Test
    void shouldReturnEmptyListWhenNoRoomsInCinema() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId))
                .thenReturn(Collections.emptyList());

        // Act
        List<ShowTimeDomainEntity> result = useCase.listAllShowTimesByCinemaId(cinemaId);

        // Assert
        assertTrue(result.isEmpty());
        verify(findingAllRoomsByCinemaIdOutputPort, times(1)).findAllByCinemaId(cinemaId);
        verify(listAllShowTimesByListRoomsIdOutputPort, times(1))
                .findAllShowTimesByListRoomsId(Collections.emptyList());
    }
}

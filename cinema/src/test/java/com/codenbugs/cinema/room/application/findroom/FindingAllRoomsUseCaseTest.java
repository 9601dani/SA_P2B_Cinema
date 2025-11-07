package com.codenbugs.cinema.room.application.findroom;

import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.application.usecase.findroom.FindingAllRoomsUseCase;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindingAllRoomsUseCaseTest {
    @Mock
    private FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;

    private FindingAllRoomsUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new FindingAllRoomsUseCase(findingAllRoomsByCinemaIdOutputPort);
    }

    @Test
    void shouldReturnEmptyListWhenNoRoomsExist() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId))
                .thenReturn(List.of());

        // Act
        List<RoomDomainEntity> result = useCase.findAllByCinemaId(cinemaId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(findingAllRoomsByCinemaIdOutputPort, times(1)).findAllByCinemaId(cinemaId);
    }

    @Test
    void shouldReturnListOfRoomsWhenRoomsExist() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        RoomDomainEntity room1 = new RoomDomainEntity(cinemaId, "img1.png", "Sala 1", 3, 3, "Descripción válida 1");
        RoomDomainEntity room2 = new RoomDomainEntity(cinemaId, "img2.png", "Sala 2", 4, 4, "Descripción válida 2");

        when(findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId))
                .thenReturn(List.of(room1, room2));

        // Act
        List<RoomDomainEntity> result = useCase.findAllByCinemaId(cinemaId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Sala 1", result.get(0).getName());
        assertEquals("Sala 2", result.get(1).getName());
        verify(findingAllRoomsByCinemaIdOutputPort, times(1)).findAllByCinemaId(cinemaId);
    }
}

package com.codenbugs.cinema.room.application.findroom;

import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByIdOutputPort;
import com.codenbugs.cinema.room.application.usecase.findroom.FindingRoomByIdUseCase;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindingRoomByIdUseCaseTest {
    @Mock
    private FindingRoomByIdOutputPort findingRoomByIdOutputPort;

    private FindingRoomByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new FindingRoomByIdUseCase(findingRoomByIdOutputPort);
    }

    @Test
    void shouldReturnRoomWhenRoomExists() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        RoomDomainEntity room = new RoomDomainEntity(
                UUID.randomUUID(),
                "img.png",
                "Sala 1",
                5,
                5,
                "Descripción válida para la sala"
        );
        when(findingRoomByIdOutputPort.findingRoomById(roomId)).thenReturn(Optional.of(room));

        // Act
        RoomDomainEntity result = useCase.findRoomById(roomId);

        // Assert
        assertNotNull(result);
        assertEquals("Sala 1", result.getName());
        verify(findingRoomByIdOutputPort, times(1)).findingRoomById(roomId);
    }

    @Test
    void shouldThrowEntityNotFountWhenRoomDoesNotExist() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        when(findingRoomByIdOutputPort.findingRoomById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class, () -> useCase.findRoomById(roomId));
        assertEquals("Sala no encontrada con el id" + roomId, exception.getMessage());
        verify(findingRoomByIdOutputPort, times(1)).findingRoomById(roomId);
    }
}

package com.codenbugs.cinema.room.application.updateroom;

import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByIdOutputPort;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByNameOutputPort;
import com.codenbugs.cinema.room.application.ports.output.UpdatingRoomByIdOutputPort;
import com.codenbugs.cinema.room.application.usecase.updateroom.UpdateRoomDto;
import com.codenbugs.cinema.room.application.usecase.updateroom.UpdatingRoomByIdUseCase;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdatingRoomByIdUseCaseTest {
    @Mock
    private UpdatingRoomByIdOutputPort updatingRoomByIdOutputPort;

    @Mock
    private FindingRoomByIdOutputPort findingRoomByIdOutputPort;

    @Mock
    private FindingRoomByNameOutputPort findingRoomByNameOutputPort;

    private UpdatingRoomByIdUseCase useCase;

    private UUID cinemaId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UpdatingRoomByIdUseCase(updatingRoomByIdOutputPort, findingRoomByIdOutputPort, findingRoomByNameOutputPort);
        cinemaId = UUID.randomUUID();
    }

    @Test
    void shouldThrowEntityNotFoundWhenRoomDoesNotExist() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        UpdateRoomDto updateDto = new UpdateRoomDto("Sala Test", "Descripción válida de sala", "img.png", true, false);
        when(findingRoomByIdOutputPort.findingRoomById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class, () -> useCase.updatingRoomById(roomId, updateDto));
        assertEquals("Sala no encontado para actualizar, Id: " + roomId, exception.getMessage());

        verify(findingRoomByIdOutputPort, times(1)).findingRoomById(roomId);
        verifyNoInteractions(findingRoomByNameOutputPort);
        verifyNoInteractions(updatingRoomByIdOutputPort);
    }

    @Test
    void shouldThrowEntityAlreadyExistsWhenRoomNameExists() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        RoomDomainEntity existingRoom = new RoomDomainEntity(roomId, cinemaId, 100, "img.png", "Sala A", 10, 10, "Descripción válida de sala", true, false);
        UpdateRoomDto updateDto = new UpdateRoomDto("Sala B", "Otra descripción válida de sala", "img.png", true, false);

        when(findingRoomByIdOutputPort.findingRoomById(roomId)).thenReturn(Optional.of(existingRoom));
        when(findingRoomByNameOutputPort.findingRoomByNameAndCinemaId("Sala B", existingRoom.getCinemaId()))
                .thenReturn(Optional.of(new RoomDomainEntity("Sala B", "Descripción válida de sala", "img.png", true, false)));

        // Act & Assert
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> useCase.updatingRoomById(roomId, updateDto));
        assertEquals("Ya existe un cine con ese nombre", exception.getMessage());

        verify(findingRoomByIdOutputPort, times(1)).findingRoomById(roomId);
        verify(findingRoomByNameOutputPort, times(1)).findingRoomByNameAndCinemaId("Sala B", existingRoom.getCinemaId());
        verifyNoInteractions(updatingRoomByIdOutputPort);
    }

    @Test
    void shouldUpdateRoomSuccessfully() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        UUID cinemaId = UUID.randomUUID();

        RoomDomainEntity existingRoom = new RoomDomainEntity(
                roomId,
                cinemaId,
                100,
                "img.png",
                "Sala A",
                10,
                10,
                "Descripción válida de sala",
                true,
                false
        );

        UpdateRoomDto updateDto = new UpdateRoomDto(
                "Sala A",
                "Descripción válida de sala",
                "img.png",
                true,
                false
        );

        when(findingRoomByIdOutputPort.findingRoomById(roomId))
                .thenReturn(Optional.of(existingRoom));

        when(updatingRoomByIdOutputPort.updatingRoomById(eq(roomId), any(RoomDomainEntity.class)))
                .thenReturn(existingRoom);

        // Act
        RoomDomainEntity result = useCase.updatingRoomById(roomId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Sala A", result.getName());
        assertEquals("Descripción válida de sala", result.getDescription());

        verify(findingRoomByIdOutputPort, times(1)).findingRoomById(roomId);
        verify(updatingRoomByIdOutputPort, times(1)).updatingRoomById(eq(roomId), any(RoomDomainEntity.class));
        verifyNoInteractions(findingRoomByNameOutputPort);
    }
}

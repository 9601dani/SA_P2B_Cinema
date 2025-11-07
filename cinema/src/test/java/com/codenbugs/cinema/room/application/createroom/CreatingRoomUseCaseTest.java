package com.codenbugs.cinema.room.application.createroom;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.room.application.usecase.createroom.CreateRoomDto;
import com.codenbugs.cinema.room.application.usecase.createroom.CreatingRoomUseCase;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreatingRoomUseCaseTest {
    @Mock
    private com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdOutputPort findingCinemaByIdOutputPort;

    @Mock
    private com.codenbugs.cinema.room.application.ports.output.StoringRoomOutputPort storingRoomOutputPort;

    @Mock
    private com.codenbugs.cinema.seat.application.ports.output.StoringAllSeatsOutputPort storingAllSeatsOutputPort;

    @Mock
    private com.codenbugs.cinema.room.application.ports.output.FindingRoomByNameOutputPort findingRoomByNameOutputPort;

    private CreatingRoomUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreatingRoomUseCase(
                findingCinemaByIdOutputPort,
                storingRoomOutputPort,
                storingAllSeatsOutputPort,
                findingRoomByNameOutputPort
        );
    }

    @Test
    void shouldCreateRoomSuccessfully() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        CreateRoomDto dto = new CreateRoomDto(cinemaId, "Sala A", "Descripción válida", 3, 3, "img.png");

        CinemaDomainEntity cinema = mock(CinemaDomainEntity.class);

        when(findingCinemaByIdOutputPort.findCinemaById(cinemaId))
                .thenReturn(Optional.of(cinema));

        when(findingRoomByNameOutputPort.findingRoomByNameAndCinemaId("Sala A", cinemaId))
                .thenReturn(Optional.empty());

        RoomDomainEntity savedRoom = mock(RoomDomainEntity.class);
        when(savedRoom.generateSeats()).thenReturn(List.of(
                new SeatDomainEntity(1, 1, "A1", UUID.randomUUID()),
                new SeatDomainEntity(2, 1, "B1", UUID.randomUUID())
        ));
        when(storingRoomOutputPort.save(any(RoomDomainEntity.class))).thenReturn(savedRoom);

        // Act
        RoomDomainEntity result = useCase.createRoom(dto);

        // Assert
        assertNotNull(result);
        verify(findingCinemaByIdOutputPort, times(1)).findCinemaById(cinemaId);
        verify(findingRoomByNameOutputPort, times(1)).findingRoomByNameAndCinemaId("Sala A", cinemaId);
        verify(storingRoomOutputPort, times(1)).save(any(RoomDomainEntity.class));
        verify(storingAllSeatsOutputPort, times(1)).saveAll(anyList());
    }

    @Test
    void shouldThrowEntityNotFoundWhenCinemaDoesNotExist() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        CreateRoomDto dto = new CreateRoomDto(cinemaId, "Sala B", "Descripción válida", 3, 3, "img.png");

        when(findingCinemaByIdOutputPort.findCinemaById(cinemaId))
                .thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class, () -> useCase.createRoom(dto));
        assertEquals("Cine no encontrado con id: " + cinemaId, exception.getMessage());

        verify(findingCinemaByIdOutputPort, times(1)).findCinemaById(cinemaId);
        verifyNoInteractions(findingRoomByNameOutputPort, storingRoomOutputPort, storingAllSeatsOutputPort);
    }

    @Test
    void shouldThrowEntityAlreadyExistsWhenRoomNameExists() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        CreateRoomDto dto = new CreateRoomDto(cinemaId, "Sala C", "Descripción válida", 3, 3, "img.png");

        CinemaDomainEntity cinema = mock(CinemaDomainEntity.class);
        RoomDomainEntity existingRoom = mock(RoomDomainEntity.class);

        when(findingCinemaByIdOutputPort.findCinemaById(cinemaId))
                .thenReturn(Optional.of(cinema));

        when(findingRoomByNameOutputPort.findingRoomByNameAndCinemaId("Sala C", cinemaId))
                .thenReturn(Optional.of(existingRoom));

        // Act & Assert
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> useCase.createRoom(dto));
        assertEquals("Ya existe una sala con ese nombre", exception.getMessage());

        verify(findingCinemaByIdOutputPort, times(1)).findCinemaById(cinemaId);
        verify(findingRoomByNameOutputPort, times(1)).findingRoomByNameAndCinemaId("Sala C", cinemaId);
        verifyNoInteractions(storingRoomOutputPort, storingAllSeatsOutputPort);
    }
}

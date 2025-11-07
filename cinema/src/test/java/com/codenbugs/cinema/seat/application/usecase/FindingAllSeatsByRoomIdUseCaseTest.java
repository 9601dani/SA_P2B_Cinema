package com.codenbugs.cinema.seat.application.usecase;

import com.codenbugs.cinema.seat.application.ports.output.FindingAllSeatsByRoomIdOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class FindingAllSeatsByRoomIdUseCaseTest {
    @InjectMocks
    private FindingAllSeatsByRoomIdUseCase useCase;

    @Mock
    private FindingAllSeatsByRoomIdOutputPort outputPort;

    private UUID roomId;
    private List<SeatDomainEntity> seats;

    @BeforeEach
    void setup() {
        roomId = UUID.randomUUID();
        seats = List.of(
                new SeatDomainEntity(1, 1, "A-01", UUID.randomUUID()),
                new SeatDomainEntity(2, 1, "A-02", UUID.randomUUID())
        );
    }

    @Test
    void shouldFindAllSeatsByRoomIdSuccessfully() {
        // Arrange
        when(outputPort.findAllSeatsByRoomId(roomId)).thenReturn(seats);

        // Act
        List<SeatDomainEntity> result = useCase.findAllSeatsByRoomId(roomId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(seats, result);
        verify(outputPort, times(1)).findAllSeatsByRoomId(roomId);
    }

    @Test
    void shouldReturnEmptyListWhenNoSeatsFound() {
        // Arrange
        when(outputPort.findAllSeatsByRoomId(roomId)).thenReturn(List.of());

        // Act
        List<SeatDomainEntity> result = useCase.findAllSeatsByRoomId(roomId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(outputPort, times(1)).findAllSeatsByRoomId(roomId);
    }

}

package com.codenbugs.cinema.seat.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.seat.application.ports.input.FindingAllSeatsByRoomIdInputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.seat.infrastructure.inputadapter.dto.SeatResponseDto;
import com.codenbugs.cinema.seat.infrastructure.inputadapter.mapper.SeatRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class SeatControllerAdapterTest {
    @InjectMocks
    private SeatControllerAdapter controller;

    @Mock
    private FindingAllSeatsByRoomIdInputPort findingAllSeatsByRoomIdInputPort;

    @Mock
    private SeatRestMapper mapper;

    private UUID roomId;
    private List<SeatDomainEntity> seatEntities;
    private List<SeatResponseDto> seatDtos;

    @BeforeEach
    void setup() {
        roomId = UUID.randomUUID();

        seatEntities = List.of(
                new SeatDomainEntity(1, 1, "A-01", UUID.randomUUID()),
                new SeatDomainEntity(1, 2, "A-02", UUID.randomUUID())
        );

        seatDtos = List.of(
                new SeatResponseDto(seatEntities.get(0).getId(), seatEntities.get(0).getRoomId(), seatEntities.get(0).getName(), seatEntities.get(0).getRowNum(), seatEntities.get(0).getColNum()),
                new SeatResponseDto(seatEntities.get(1).getId(), seatEntities.get(1).getRoomId(), seatEntities.get(1).getName(), seatEntities.get(1).getRowNum(), seatEntities.get(1).getColNum())
        );
    }

    @Test
    void shouldReturnSeatsByRoomIdSuccessfully() {
        // Arrange
        when(findingAllSeatsByRoomIdInputPort.findAllSeatsByRoomId(roomId)).thenReturn(seatEntities);
        when(mapper.responseDto(seatEntities.get(0))).thenReturn(seatDtos.get(0));
        when(mapper.responseDto(seatEntities.get(1))).thenReturn(seatDtos.get(1));

        // Act
        ResponseEntity<List<SeatResponseDto>> response = controller.findSeatsByRoomId(roomId);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getBody().size());
        assertEquals(seatDtos, response.getBody());

        verify(findingAllSeatsByRoomIdInputPort, times(1)).findAllSeatsByRoomId(roomId);
        verify(mapper, times(1)).responseDto(seatEntities.get(0));
        verify(mapper, times(1)).responseDto(seatEntities.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenNoSeatsFound() {
        // Arrange
        when(findingAllSeatsByRoomIdInputPort.findAllSeatsByRoomId(roomId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<SeatResponseDto>> response = controller.findSeatsByRoomId(roomId);

        // Assert
        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());

        verify(findingAllSeatsByRoomIdInputPort, times(1)).findAllSeatsByRoomId(roomId);
        verifyNoInteractions(mapper);
    }
}

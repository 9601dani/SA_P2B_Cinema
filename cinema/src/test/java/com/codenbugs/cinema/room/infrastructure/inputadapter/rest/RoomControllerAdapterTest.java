package com.codenbugs.cinema.room.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.room.application.ports.input.*;
import com.codenbugs.cinema.room.application.usecase.createroom.CreateRoomDto;
import com.codenbugs.cinema.room.application.usecase.updateroom.UpdateRoomDto;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.CreateRoomRequestDto;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.RoomResponseDto;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.UpdateRoomRequestDto;
import com.codenbugs.cinema.room.infrastructure.inputadapter.mapper.RoomRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class RoomControllerAdapterTest {
    @InjectMocks
    private RoomControllerAdapter controller;

    @Mock
    private CreatingRoomInputPort creatingRoomInputPort;

    @Mock
    private FindingRoomByIdInputPort findingRoomByIdInputPort;

    @Mock
    private FindingAllRoomsByCinemaIdInputPort findingAllRoomsByCinemaIdInputPort;

    @Mock
    private UpdatingRoomByIdInputPort updatingRoomByIdInputPort;

    @Mock
    private RoomRestMapper roomRestMapper;

    @Test
    void shouldCreateRoomSuccessfully() {
        // Arrange
        CreateRoomRequestDto requestDto = new CreateRoomRequestDto(
                UUID.randomUUID(), "Sala Test", "Descripción válida sala", 5, 5, "img.png"
        );
        CreateRoomDto createRoomDto = requestDto.toDomain();
        RoomDomainEntity domainEntity = mock(RoomDomainEntity.class);
        RoomResponseDto responseDto = mock(RoomResponseDto.class);

        when(creatingRoomInputPort.createRoom(createRoomDto)).thenReturn(domainEntity);
        when(roomRestMapper.toRoomResponseDto(domainEntity)).thenReturn(responseDto);

        // Act
        ResponseEntity<RoomResponseDto> result = controller.createRoom(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(201, result.getStatusCodeValue());
        assertEquals(responseDto, result.getBody());
        verify(creatingRoomInputPort).createRoom(createRoomDto);
        verify(roomRestMapper).toRoomResponseDto(domainEntity);
    }

    @Test
    void shouldFindRoomByIdSuccessfully() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        RoomDomainEntity domainEntity = mock(RoomDomainEntity.class);
        RoomResponseDto responseDto = mock(RoomResponseDto.class);

        when(findingRoomByIdInputPort.findRoomById(roomId)).thenReturn(domainEntity);
        when(roomRestMapper.toRoomResponseDto(domainEntity)).thenReturn(responseDto);

        // Act
        ResponseEntity<RoomResponseDto> result = controller.findRoomById(roomId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(responseDto, result.getBody());
        verify(findingRoomByIdInputPort).findRoomById(roomId);
        verify(roomRestMapper).toRoomResponseDto(domainEntity);
    }

    @Test
    void shouldFindAllRoomsByCinemaIdSuccessfully() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        RoomDomainEntity room1 = mock(RoomDomainEntity.class);
        RoomDomainEntity room2 = mock(RoomDomainEntity.class);
        RoomResponseDto dto1 = mock(RoomResponseDto.class);
        RoomResponseDto dto2 = mock(RoomResponseDto.class);

        when(findingAllRoomsByCinemaIdInputPort.findAllByCinemaId(cinemaId))
                .thenReturn(List.of(room1, room2));
        when(roomRestMapper.toRoomResponseDto(room1)).thenReturn(dto1);
        when(roomRestMapper.toRoomResponseDto(room2)).thenReturn(dto2);

        // Act
        ResponseEntity<List<RoomResponseDto>> result = controller.findAllRoomByCinemaId(cinemaId);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(List.of(dto1, dto2), result.getBody());
        verify(findingAllRoomsByCinemaIdInputPort).findAllByCinemaId(cinemaId);
        verify(roomRestMapper).toRoomResponseDto(room1);
        verify(roomRestMapper).toRoomResponseDto(room2);
    }

    @Test
    void shouldUpdateRoomSuccessfully() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        UpdateRoomRequestDto requestDto = new UpdateRoomRequestDto(
                "Sala Actualizada",
                "Descripción válida actualizada",
                "img_updated.png",
                true,
                false
        );
        UpdateRoomDto updateDto = requestDto.toDomain();
        RoomDomainEntity updatedDomain = mock(RoomDomainEntity.class);
        RoomResponseDto responseDto = mock(RoomResponseDto.class);

        when(updatingRoomByIdInputPort.updatingRoomById(roomId, updateDto)).thenReturn(updatedDomain);
        when(roomRestMapper.toRoomResponseDto(updatedDomain)).thenReturn(responseDto);

        // Act
        ResponseEntity<RoomResponseDto> result = controller.updateRoom(roomId, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(responseDto, result.getBody());
        verify(updatingRoomByIdInputPort).updatingRoomById(roomId, updateDto);
        verify(roomRestMapper).toRoomResponseDto(updatedDomain);
    }
}

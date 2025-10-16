package com.codenbugs.cinema.room.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.common.infrastructure.annotation.WebAdapter;
import com.codenbugs.cinema.room.application.ports.input.CreatingRoomInputPort;
import com.codenbugs.cinema.room.application.ports.input.FindingAllRoomsByCinemaIdInputPort;
import com.codenbugs.cinema.room.application.ports.input.UpdatingRoomByIdInputPort;
import com.codenbugs.cinema.room.application.usecase.createroom.CreateRoomDto;
import com.codenbugs.cinema.room.application.usecase.updateroom.UpdateRoomDto;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.CreateRoomRequestDto;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.RoomResponseDto;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.UpdateRoomRequestDto;
import com.codenbugs.cinema.room.infrastructure.inputadapter.mapper.RoomRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/rooms")
@WebAdapter
@RequiredArgsConstructor
public class RoomControllerAdapter {

    private final CreatingRoomInputPort creatingRoomInputPort;
    private final RoomRestMapper roomRestMapper;
    private final FindingAllRoomsByCinemaIdInputPort findingAllRoomsByCinemaIdInputPort;
    private final UpdatingRoomByIdInputPort updatingRoomByIdInputPort;

    @PostMapping
    @Transactional
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody @Valid CreateRoomRequestDto requestDto){
        CreateRoomDto createRoomDto = requestDto.toDomain();
        RoomDomainEntity domainEntity = creatingRoomInputPort.createRoom(createRoomDto);
        RoomResponseDto responseDto = roomRestMapper.toRoomResponseDto(domainEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("cinema/{cinemaId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<RoomResponseDto>> findAllRoomByCinemaId(@PathVariable UUID cinemaId){
        List<RoomResponseDto> listRooms = findingAllRoomsByCinemaIdInputPort.findAllByCinemaId(cinemaId)
                .stream()
                .map(roomRestMapper::toRoomResponseDto)
                .toList();

        return ResponseEntity.ok(listRooms);
    }

    @PutMapping("{roomId}")
    @Transactional()
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable UUID roomId, @RequestBody @Valid UpdateRoomRequestDto requestDto){
        UpdateRoomDto updateRoomDto = requestDto.toDomain();
        RoomDomainEntity roomDomain = updatingRoomByIdInputPort.updatingRoomById(roomId, updateRoomDto);
        RoomResponseDto responseDto = roomRestMapper.toRoomResponseDto(roomDomain);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


}


package com.codenbugs.cinema.seat.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.common.infrastructure.annotation.WebAdapter;
import com.codenbugs.cinema.seat.application.ports.input.FindingAllSeatsByRoomIdInputPort;
import com.codenbugs.cinema.seat.infrastructure.inputadapter.dto.SeatResponseDto;
import com.codenbugs.cinema.seat.infrastructure.inputadapter.mapper.SeatRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/seats")
@WebAdapter
@RequiredArgsConstructor
public class SeatControllerAdapter {

    private final FindingAllSeatsByRoomIdInputPort findingAllSeatsByRoomIdInputPort;
    private final SeatRestMapper mapper;

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<SeatResponseDto>> findSeatsByRoomId(@PathVariable UUID roomId) {
        List<SeatResponseDto> seats = findingAllSeatsByRoomIdInputPort.findAllSeatsByRoomId(roomId)
                .stream()
                .map(mapper::responseDto)
                .toList();

        return ResponseEntity.ok(seats);
    }
}


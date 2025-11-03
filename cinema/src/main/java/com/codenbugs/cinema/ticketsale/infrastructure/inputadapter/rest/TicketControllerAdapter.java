package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.common.infrastructure.annotation.WebAdapter;
import com.codenbugs.cinema.ticketsale.application.ports.input.*;
import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdateStateCaseDto;
import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdatingSeatCaseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.CreateTicketRequestDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.TicketResponseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.UpdateSeatRequestDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.UpdateStateRequestDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.mapper.TicketRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/ad-tickets")
@WebAdapter
@RequiredArgsConstructor
public class TicketControllerAdapter {

    private final CreatingTicketInputPort creatingTicketInputPort;
    private final UpdatingStateByIdInputPort updatingStateByIdInputPort;
    private final ListAllTicketByUserIdInputPort listAllTicketByUserIdInputPort;
    private final ListAllTicketsByShowTimeIdInputPort listAllTicketsByShowTimeIdInputPort;
    private final ListAllTicketsByUserIdAndShowTimeIdInputPort listAllTicketsByUserIdAndShowTimeIdInputPort;
    private final UpdatingSeatByTicketIdInputPort updatingSeatByTicketIdInputPort;
    private final TicketRestMapper ticketRestMapper;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createTicket(@RequestBody @Valid CreateTicketRequestDto dto) {
        creatingTicketInputPort.createTicket(dto.toCaseDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/state/{id}")
    @Transactional
    public ResponseEntity<Void> updateStateById(@RequestBody @Valid UpdateStateRequestDto requestDto, @PathVariable UUID id) {
        UpdateStateCaseDto updateStateCaseDto = requestDto.toCase();
        updatingStateByIdInputPort.updateStateById(updateStateCaseDto, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<TicketResponseDto>> listAllTicketByUserId(@PathVariable UUID id) {
        var tickets = listAllTicketByUserIdInputPort.listAllTicketsByUserId(id)
                .stream()
                .map(ticketRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/show-time/{id}")
    public ResponseEntity<List<TicketResponseDto>> listAllTicketsByShowTimeId(@PathVariable UUID id) {
        var tickets = listAllTicketsByShowTimeIdInputPort.listAllTicketsByShowTimeId(id)
                .stream()
                .map(ticketRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/customer/{userId}/show-time/{showTimeId}")
    public ResponseEntity<List<TicketResponseDto>> listAllTicketsByUserIdAndShowTimeId(@PathVariable UUID userId, @PathVariable UUID showTimeId) {
        var tickets = listAllTicketsByUserIdAndShowTimeIdInputPort.listAllTicketsByUserIdAndShoTimeId(userId, showTimeId)
                .stream()
                .map(ticketRestMapper::toResponseDto)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/seat/{id}")
    @Transactional
    public ResponseEntity<Void> updateSeatByTicketId(@RequestBody @Valid UpdateSeatRequestDto requestDto, @PathVariable UUID id) {
        UpdatingSeatCaseDto updateStateCaseDto = requestDto.toCaseDto();
        updatingSeatByTicketIdInputPort.updateSeatByTicketId(updateStateCaseDto, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

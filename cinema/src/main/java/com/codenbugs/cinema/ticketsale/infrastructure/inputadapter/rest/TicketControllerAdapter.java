package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.rest;

import com.codenbugs.cinema.common.infrastructure.annotation.WebAdapter;
import com.codenbugs.cinema.ticketsale.application.ports.input.CreatingTicketInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.input.UpdatingStateByIdInputPort;
import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdateStateCaseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.CreateTicketRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/ad-tickets")
@WebAdapter
@RequiredArgsConstructor
public class TicketControllerAdapter {

    private final CreatingTicketInputPort creatingTicketInputPort;
    private final UpdatingStateByIdInputPort updatingStateByIdInputPort;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> createTicket(@RequestBody @Valid CreateTicketRequestDto dto) {
        creatingTicketInputPort.createTicket(dto.toCaseDto());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/state/{id}")
    @Transactional
    public ResponseEntity<Void> updateStateById(@RequestBody @Valid UpdateStateCaseDto updateStateCaseDto, @PathVariable UUID id) {
        updatingStateByIdInputPort.updateStateById(updateStateCaseDto, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}

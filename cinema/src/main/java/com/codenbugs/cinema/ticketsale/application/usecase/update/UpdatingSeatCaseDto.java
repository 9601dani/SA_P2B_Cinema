package com.codenbugs.cinema.ticketsale.application.usecase.update;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class UpdatingSeatCaseDto {
    UUID id;
    UUID seatId; // new seat ID
}

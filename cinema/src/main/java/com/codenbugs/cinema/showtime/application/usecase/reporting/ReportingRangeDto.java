package com.codenbugs.cinema.showtime.application.usecase.reporting;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@AllArgsConstructor
public class ReportingRangeDto {
    UUID cinemaId;
    LocalDate startDate;
    LocalDate endDate;
}

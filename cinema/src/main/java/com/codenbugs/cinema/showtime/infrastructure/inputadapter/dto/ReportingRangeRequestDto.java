package com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto;


import com.codenbugs.cinema.showtime.application.usecase.reporting.ReportingRangeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
public record ReportingRangeRequestDto(
        @NotNull
        UUID targetId,
        @NotBlank
        String startDate,
        @NotBlank
        String endDate
) {
    public ReportingRangeDto toCase(){
        return new ReportingRangeDto(
                this.targetId,
                LocalDate.parse(this.startDate),
                LocalDate.parse(this.endDate)
        );
    }
}
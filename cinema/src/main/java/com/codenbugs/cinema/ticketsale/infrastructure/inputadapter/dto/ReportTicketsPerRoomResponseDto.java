package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record ReportTicketsPerRoomResponseDto(
        UUID roomId,
        Integer capacity,
        String imageUrl,
        String name,
        Integer rows,
        Integer columns,
        List<TicketsReportResponseDto> ticketsSold
) {
}

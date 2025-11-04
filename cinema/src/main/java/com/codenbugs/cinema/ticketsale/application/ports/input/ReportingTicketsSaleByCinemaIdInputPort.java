package com.codenbugs.cinema.ticketsale.application.ports.input;


import com.codenbugs.cinema.ticketsale.domain.model.ReportTicketsPerRoomEntityDomain;

import java.util.List;
import java.util.UUID;

public interface ReportingTicketsSaleByCinemaIdInputPort {
    List<ReportTicketsPerRoomEntityDomain> reportTicketsSaleByCinemaId(UUID cinemaId);
}

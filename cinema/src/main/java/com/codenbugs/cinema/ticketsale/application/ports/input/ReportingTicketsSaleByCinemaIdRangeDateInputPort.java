package com.codenbugs.cinema.ticketsale.application.ports.input;

import com.codenbugs.cinema.showtime.application.usecase.reporting.ReportingRangeDto;
import com.codenbugs.cinema.ticketsale.domain.model.ReportTicketsPerRoomEntityDomain;

import java.util.List;

public interface ReportingTicketsSaleByCinemaIdRangeDateInputPort {
    List<ReportTicketsPerRoomEntityDomain> reportTicketsSaleByCinemaIdRangeDate(ReportingRangeDto reportingRangeDto);
}

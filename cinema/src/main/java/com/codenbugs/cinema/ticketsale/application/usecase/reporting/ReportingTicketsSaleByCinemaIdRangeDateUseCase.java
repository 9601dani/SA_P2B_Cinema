package com.codenbugs.cinema.ticketsale.application.usecase.reporting;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.seat.application.ports.output.FindingAllSeatsByListRoomIdsOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdOutputPort;
import com.codenbugs.cinema.showtime.application.usecase.reporting.ReportingRangeDto;
import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.ticketsale.application.ports.input.ReportingTicketsSaleByCinemaIdRangeDateInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingAllCustomersOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingAllMoviesOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketsByListShowTimesIdRangDateOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.CustomerDomainEntity;
import com.codenbugs.cinema.ticketsale.domain.model.ReportTicketsPerRoomEntityDomain;
import com.codenbugs.cinema.ticketsale.domain.model.TicketsReportDomainEntity;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class ReportingTicketsSaleByCinemaIdRangeDateUseCase implements ReportingTicketsSaleByCinemaIdRangeDateInputPort {

    private final FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;
    private final ListAllShowTimesByListRoomsIdOutputPort listAllShowTimesByListRoomsIdOutputPort;
    private final ListAllTicketsByListShowTimesIdRangDateOutputPort listAllTicketsByListShowTimesIdRangDateOutputPort;
    private final FindingAllMoviesOutputPort findingAllMoviesOutputPort;
    private final FindingAllCustomersOutputPort findingAllCustomersOutputPort;
    private final FindingAllSeatsByListRoomIdsOutputPort findingAllSeatsByListRoomIdsOutputPort;

    @Override
    public List<ReportTicketsPerRoomEntityDomain> reportTicketsSaleByCinemaIdRangeDate(ReportingRangeDto reportingRangeDto) {

        // Obtener rooms
        var rooms = findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(reportingRangeDto.getCinemaId());
        if (rooms.isEmpty()) return List.of();

        var roomsId = rooms.stream()
                .map(RoomDomainEntity::getId)
                .toList();

        // Obtener seats por lista de rooms
        var seats = findingAllSeatsByListRoomIdsOutputPort.findAllSeatsByListRoomIds(roomsId);

        // Map para acceso rápido por seatId -> SeatDomainEntity
        var seatsMap = seats.stream()
                .collect(Collectors.toMap(SeatDomainEntity::getId, s -> s));

        // Obtener showtimes por rooms
        var showTimes = listAllShowTimesByListRoomsIdOutputPort.findAllShowTimesByListRoomsId(roomsId);
        if (showTimes.isEmpty()) return List.of();

        var showTimeMap = showTimes.stream()
                .collect(Collectors.toMap(ShowTimeDomainEntity::getId, st -> st));

        var showTimesIds = new ArrayList<>(showTimeMap.keySet());

        Instant startDate = reportingRangeDto.getStartDate().atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endDate = reportingRangeDto.getEndDate().atStartOfDay(ZoneOffset.UTC).toInstant();

        // Obtener tickets de todos los showtimes y rangos
        var tickets = listAllTicketsByListShowTimesIdRangDateOutputPort
                .findAllTicketsByListShowTimesIdRangDate(showTimesIds, startDate, endDate);

        // Obtener movies y customers
        var movies = findingAllMoviesOutputPort.findAllMoviesByList();
        var customers = findingAllCustomersOutputPort.findingAllCustomers();

        var moviesMap = movies.stream()
                .collect(Collectors.toMap(MovieDomainEntity::getId, m -> m));

        var customersMap = customers.stream()
                .collect(Collectors.toMap(CustomerDomainEntity::getUserId, c -> c));

        // Agrupar tickets por room
        var ticketsByRoom = tickets.stream()
                .collect(Collectors.groupingBy(t -> showTimeMap.get(t.getShowtimeId()).getRoomId()));

        // Construir respuesta final
        List<ReportTicketsPerRoomEntityDomain> result = new ArrayList<>();

        for (RoomDomainEntity room : rooms) {

            var report = new ReportTicketsPerRoomEntityDomain(
                    room.getId(),
                    room.getCapacity(),
                    room.getImageUrl(),
                    room.getName(),
                    room.getRows(),
                    room.getColumns()
            );

            // tickets de esta sala
            var ticketsRoomList = ticketsByRoom.getOrDefault(room.getId(), List.of());

            List<TicketsReportDomainEntity> ticketsReport = ticketsRoomList.stream()
                    .map(ticket -> {

                        var showtime = showTimeMap.get(ticket.getShowtimeId());
                        var movie = moviesMap.get(showtime.getMovieId());
                        var customer = customersMap.get(ticket.getUserId());
                        var seat = seatsMap.get(ticket.getSeatId());

                        return new TicketsReportDomainEntity(ticket.getId(),
                                seat != null ? seat.getName() : "Asiento desconocido",
                                customer != null ? customer.getFullName() : "Cliente desconocido",
                                ticket.getShowtimeId(),
                                movie != null ? movie.getTitle() : "Desconocida",
                                showtime.getStartTime(),
                                showtime.getEndTime(),
                                ticket.getPurchaseDate(),
                                ticket.getPrice(),
                                ticket.getDiscountPercentage(),
                                ticket.getPriceTotal());
                    })
                    .toList();

            report.setTicketsSold(ticketsReport);

            result.add(report);
        }

        return result;
    }
}

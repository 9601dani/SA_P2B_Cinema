package com.codenbugs.cinema.ticketsale.application.ports.output;

public interface UpdatingSeatByTicketIdOutputPort {
    void updateSeatByTicketIdInDatabase(java.util.UUID seatId, java.util.UUID ticketId);
}

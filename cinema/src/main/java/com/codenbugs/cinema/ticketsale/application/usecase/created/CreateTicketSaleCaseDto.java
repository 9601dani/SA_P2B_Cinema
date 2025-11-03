package com.codenbugs.cinema.ticketsale.application.usecase.created;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class CreateTicketSaleCaseDto {
    UUID walletId;
    UUID showtimeId;
    UUID seatId;
    UUID userId;
    UUID promotionId;

    public TicketSaleDomainEntity toDomain() {
        return new TicketSaleDomainEntity(
                showtimeId,
                seatId,
                userId,
                walletId,
                promotionId
        );
    }
}

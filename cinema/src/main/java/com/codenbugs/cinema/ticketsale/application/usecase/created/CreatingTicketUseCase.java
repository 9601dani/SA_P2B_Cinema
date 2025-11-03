package com.codenbugs.cinema.ticketsale.application.usecase.created;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.common.infrastructure.exception.BadRequestException;
import com.codenbugs.cinema.seat.application.ports.output.FindingSeatByIdOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.output.FindingShowTimeByIdOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.ticketsale.application.ports.input.CreatingTicketInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.*;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.UUID;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreatingTicketUseCase implements CreatingTicketInputPort {

    private final StoringTicketOutputPort storingTicketOutputPort;
    private final FindingShowTimeByIdOutputPort findingShowTimeByIdOutputPort;
    private final FindingSeatByIdOutputPort findingSeatByIdOutputPort;
    private final FindingPromotionByIdOutputPort findingPromotionByIdOutputPort;
    private final FindingTicketBySeatIdAndShowTimeIdOutputPort findingTicketBySeatIdAndShowTimeIdOutputPort;
    private final PayTicketEventPort payTicketEventPort;
    private final UpdatingStateByIdOutputPort updatingStateByIdOutputPort;

    @Override
    @Transactional
    public void createTicket(CreateTicketSaleCaseDto command) {
        TicketSaleDomainEntity domainEntity = command.toDomain();

        // validar existencia y vigencia de la funcion
        ShowTimeDomainEntity showTime = findingShowTimeByIdOutputPort.findById(domainEntity.getShowtimeId())
                .orElseThrow(() -> new EntityNotFount("Funcion no encontrado para el ticket."));
        showTime.validateActiveAndCurrentDate();

        // validar que el asiento exista y que no este ocupado en esa funcion
        SeatDomainEntity seat = findingSeatByIdOutputPort.findById(domainEntity.getSeatId())
                .orElseThrow(() -> new EntityNotFount("Asiento no encontrado para el ticket."));

        TicketSaleDomainEntity existingTicket = findingTicketBySeatIdAndShowTimeIdOutputPort
                .findBySeatIdAndShowTimeId(seat.getId(), domainEntity.getShowtimeId())
                .orElse(null);

        if (existingTicket != null && existingTicket.getState() != StateTicket.REJECTED) {
            throw new EntityAlreadyExistsException("El asiento ya está ocupado para esta función.");
        }

        // obtener el porcentaje de descuento si aplica
        BigDecimal discountPercentage = BigDecimal.ZERO;
        if (domainEntity.getPromotionId() != null) {
            var promotion = findingPromotionByIdOutputPort.findingPromotionById(domainEntity.getPromotionId());
            discountPercentage = promotion.getDiscountPercentage();
        }


        // crear nuevo dominio con los datos completos
        TicketSaleDomainEntity domainEntityComplete = new TicketSaleDomainEntity(
                domainEntity.getShowtimeId(),
                domainEntity.getSeatId(),
                domainEntity.getUserId(),
                showTime.getPrice(),
                discountPercentage,
                domainEntity.getState(),
                domainEntity.getWalletId(),
                domainEntity.getPromotionId()
        );

        UUID idTicket = storingTicketOutputPort.save(domainEntityComplete);
        payTicketEventPort.payTicketEvent(domainEntityComplete, idTicket);
        try {
            payTicketEventPort.payTicketEvent(domainEntityComplete, idTicket);
        } catch (Exception e) {
            updatingStateByIdOutputPort.updateStateById(idTicket, StateTicket.REJECTED);
            throw new BadRequestException("Error al procesar el pago del ticket");
        }
    }
}

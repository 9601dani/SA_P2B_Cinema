package com.codenbugs.cinema.ticketsale.application.usecase.list;

import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketsByUserIdAndShowTimeIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllTicketsByUserIdAndShowTimeIdUseCaseTest {
    private ListAllTicketsByUserIdAndShowTimeIdOutputPort outputPort;
    private ListAllTicketsByUserIdAndShowTimeIdUseCase useCase;

    @BeforeEach
    void setUp() {
        outputPort = mock(ListAllTicketsByUserIdAndShowTimeIdOutputPort.class);
        useCase = new ListAllTicketsByUserIdAndShowTimeIdUseCase(outputPort);
    }

    @Test
    void shouldReturnTicketsWhenUserHasTicketsForShowTime() {
        UUID userId = UUID.randomUUID();
        UUID showTimeId = UUID.randomUUID();

        TicketSaleDomainEntity ticket1 = new TicketSaleDomainEntity(
                showTimeId, UUID.randomUUID(), userId,
                BigDecimal.valueOf(50), BigDecimal.ZERO, null, UUID.randomUUID(), null
        );
        TicketSaleDomainEntity ticket2 = new TicketSaleDomainEntity(
                showTimeId, UUID.randomUUID(), userId,
                BigDecimal.valueOf(70), BigDecimal.ZERO, null, UUID.randomUUID(), null
        );

        when(outputPort.listAllTicketsByUserIdAndShowTimeId(userId, showTimeId))
                .thenReturn(List.of(ticket1, ticket2));

        List<TicketSaleDomainEntity> result = useCase.listAllTicketsByUserIdAndShoTimeId(userId, showTimeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(ticket1));
        assertTrue(result.contains(ticket2));
        verify(outputPort, times(1)).listAllTicketsByUserIdAndShowTimeId(userId, showTimeId);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoTicketsForShowTime() {
        UUID userId = UUID.randomUUID();
        UUID showTimeId = UUID.randomUUID();

        when(outputPort.listAllTicketsByUserIdAndShowTimeId(userId, showTimeId))
                .thenReturn(List.of());

        List<TicketSaleDomainEntity> result = useCase.listAllTicketsByUserIdAndShoTimeId(userId, showTimeId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(outputPort, times(1)).listAllTicketsByUserIdAndShowTimeId(userId, showTimeId);
    }
}

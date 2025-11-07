package com.codenbugs.cinema.ticketsale.application.usecase.list;

import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketByUserIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllTicketUserIdUseCaseTest {
    private ListAllTicketByUserIdOutputPort outputPort;
    private ListAllTicketByUserIdUseCase useCase;

    @BeforeEach
    void setUp() {
        outputPort = mock(ListAllTicketByUserIdOutputPort.class);
        useCase = new ListAllTicketByUserIdUseCase(outputPort);
    }

    @Test
    void shouldReturnTicketsWhenUserHasTickets() {
        UUID userId = UUID.randomUUID();
        TicketSaleDomainEntity ticket1 = new TicketSaleDomainEntity(
                UUID.randomUUID(), UUID.randomUUID(), userId,
                BigDecimal.valueOf(50), BigDecimal.ZERO, null, UUID.randomUUID(), null
        );
        TicketSaleDomainEntity ticket2 = new TicketSaleDomainEntity(
                UUID.randomUUID(), UUID.randomUUID(), userId,
                BigDecimal.valueOf(70), BigDecimal.ZERO, null, UUID.randomUUID(), null
        );

        when(outputPort.listAllTicketsByUserId(userId)).thenReturn(List.of(ticket1, ticket2));

        List<TicketSaleDomainEntity> result = useCase.listAllTicketsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(ticket1));
        assertTrue(result.contains(ticket2));
        verify(outputPort, times(1)).listAllTicketsByUserId(userId);
    }

    @Test
    void shouldReturnEmptyListWhenUserHasNoTickets() {
        UUID userId = UUID.randomUUID();

        when(outputPort.listAllTicketsByUserId(userId)).thenReturn(List.of());

        List<TicketSaleDomainEntity> result = useCase.listAllTicketsByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(outputPort, times(1)).listAllTicketsByUserId(userId);
    }
}

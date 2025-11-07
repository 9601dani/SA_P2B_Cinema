package com.codenbugs.cinema.ticketsale.application.usecase.list;

import com.codenbugs.cinema.ticketsale.application.ports.output.ListAllTicketsByShowTimeIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListAllTicketsByShowTimeIdUseCaseTest {
    private ListAllTicketsByShowTimeIdOutputPort outputPort;
    private ListAllTicketsByShowTimeIdUseCase useCase;

    @BeforeEach
    void setUp() {
        outputPort = mock(ListAllTicketsByShowTimeIdOutputPort.class);
        useCase = new ListAllTicketsByShowTimeIdUseCase(outputPort);
    }

    @Test
    void shouldReturnTicketsWhenThereAreTicketsForShowTime() {
        UUID showTimeId = UUID.randomUUID();

        TicketSaleDomainEntity ticket1 = new TicketSaleDomainEntity(
                showTimeId, UUID.randomUUID(), UUID.randomUUID(),
                BigDecimal.valueOf(50), BigDecimal.ZERO, null, UUID.randomUUID(), null
        );
        TicketSaleDomainEntity ticket2 = new TicketSaleDomainEntity(
                showTimeId, UUID.randomUUID(), UUID.randomUUID(),
                BigDecimal.valueOf(70), BigDecimal.ZERO, null, UUID.randomUUID(), null
        );

        when(outputPort.listAllTicketsByShowTimeId(showTimeId))
                .thenReturn(List.of(ticket1, ticket2));

        List<TicketSaleDomainEntity> result = useCase.listAllTicketsByShowTimeId(showTimeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(ticket1));
        assertTrue(result.contains(ticket2));
        verify(outputPort, times(1)).listAllTicketsByShowTimeId(showTimeId);
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoTicketsForShowTime() {
        UUID showTimeId = UUID.randomUUID();

        when(outputPort.listAllTicketsByShowTimeId(showTimeId))
                .thenReturn(List.of());

        List<TicketSaleDomainEntity> result = useCase.listAllTicketsByShowTimeId(showTimeId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(outputPort, times(1)).listAllTicketsByShowTimeId(showTimeId);
    }
}

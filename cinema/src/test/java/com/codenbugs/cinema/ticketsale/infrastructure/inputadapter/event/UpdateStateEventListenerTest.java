package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.event;

import com.codenbugs.cinema.ticketsale.application.ports.input.UpdatingStateByIdInputPort;
import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdateStateCaseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateStateEventListenerTest {
    private UpdatingStateByIdInputPort updatingStateByIdInputPort;
    private UpdateStateEventListener listener;

    @BeforeEach
    void setUp() {
        updatingStateByIdInputPort = mock(UpdatingStateByIdInputPort.class);
        listener = new UpdateStateEventListener(updatingStateByIdInputPort);
    }

    @Test
    void shouldCallUpdateStateByIdWithCorrectDto() {
        // Arrange
        UUID ticketId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        String state = "COMPLETED";
        boolean createEventPayment = true;

        Map<String, Object> payload = Map.of(
                "ticketId", ticketId.toString(),
                "state", state,
                "walletId", walletId.toString(),
                "createEventPayment", String.valueOf(createEventPayment)
        );

        ArgumentCaptor<UpdateStateCaseDto> captor = ArgumentCaptor.forClass(UpdateStateCaseDto.class);

        // Act
        listener.handleNewShowTime(payload);

        // Assert
        verify(updatingStateByIdInputPort, times(1))
                .updateStateById(captor.capture(), eq(createEventPayment));

        UpdateStateCaseDto dto = captor.getValue();
        assertEquals(ticketId, dto.getId());
        assertEquals(state, dto.getState());
        assertEquals(walletId, dto.getWalletId());
    }

    @Test
    void shouldNotThrowExceptionWhenPayloadIsInvalid() {
        // Arrange
        Map<String, Object> payload = Map.of(); // empty payload

        // Act & Assert
        assertDoesNotThrow(() -> listener.handleNewShowTime(payload));
        verifyNoInteractions(updatingStateByIdInputPort);
    }
}

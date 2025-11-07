package com.codenbugs.cinema.showtime.application.usecase.updateactive;

import com.codenbugs.cinema.showtime.application.usecase.updateactive.UpdateActiveCase;
import com.codenbugs.cinema.showtime.application.ports.output.UpdatingShowTimeActiveByIdOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class UpdatingShowTimeActiveByIdUseCaseTest {
    @Mock
    private UpdatingShowTimeActiveByIdOutputPort outputPort;

    private UpdatingShowTimeActiveByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UpdatingShowTimeActiveByIdUseCase(outputPort);
    }

    @Test
    void shouldCallOutputPortToActivateShowTime() {
        // Arrange
        UUID showTimeId = UUID.randomUUID();
        UpdateActiveCase updateActiveCase = new UpdateActiveCase(true, showTimeId);

        // Act
        useCase.updateActive(updateActiveCase);

        // Assert
        verify(outputPort, times(1)).updateActive(true, showTimeId);
    }

    @Test
    void shouldCallOutputPortToDeactivateShowTime() {
        // Arrange
        UUID showTimeId = UUID.randomUUID();
        UpdateActiveCase updateActiveCase = new UpdateActiveCase( false, showTimeId);

        // Act
        useCase.updateActive(updateActiveCase);

        // Assert
        verify(outputPort, times(1)).updateActive(false, showTimeId);
    }
}

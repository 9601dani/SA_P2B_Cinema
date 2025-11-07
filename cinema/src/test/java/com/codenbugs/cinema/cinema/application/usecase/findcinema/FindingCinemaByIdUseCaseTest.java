package com.codenbugs.cinema.cinema.application.usecase.findcinema;

import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindingCinemaByIdUseCaseTest {
    private FindingCinemaByIdOutputPort outputPort;
    private FindingCinemaByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        outputPort = mock(FindingCinemaByIdOutputPort.class);
        useCase = new FindingCinemaByIdUseCase(outputPort);
    }

    @Test
    void shouldReturnCinemaWhenFound() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        CinemaDomainEntity cinema = new CinemaDomainEntity(
                cinemaId,
                "CINE TEST",
                "img.png",
                "Address 123",
                UUID.randomUUID(),
                BigDecimal.valueOf(100),
                Instant.now()
        );

        when(outputPort.findCinemaById(cinemaId)).thenReturn(Optional.of(cinema));

        // Act
        CinemaDomainEntity result = useCase.findCinemaById(cinemaId);

        // Assert
        assertNotNull(result);
        assertEquals(cinema.getId(), result.getId());
        assertEquals(cinema.getName(), result.getName());
        verify(outputPort, times(1)).findCinemaById(cinemaId);
    }

    @Test
    void shouldThrowExceptionWhenCinemaNotFound() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        when(outputPort.findCinemaById(cinemaId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class,
                () -> useCase.findCinemaById(cinemaId));

        assertEquals("Cine No encontrado con id: " + cinemaId, exception.getMessage());
        verify(outputPort, times(1)).findCinemaById(cinemaId);
    }
}

package com.codenbugs.cinema.cinema.application.usecase.findcinema;

import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdAdminOutputPort;
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

public class FindingCinemaByIdAdminUseCaseTest {
    private FindingCinemaByIdAdminOutputPort outputPort;
    private FindingCinemaByIdAdminUseCase useCase;

    @BeforeEach
    void setUp() {
        outputPort = mock(FindingCinemaByIdAdminOutputPort.class);
        useCase = new FindingCinemaByIdAdminUseCase(outputPort);
    }

    @Test
    void shouldReturnCinemaWhenFound() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        CinemaDomainEntity cinema = new CinemaDomainEntity(
                UUID.randomUUID(),
                "CINE TEST",
                "img.png",
                "Address 123",
                adminId,
                BigDecimal.valueOf(100),
                Instant.now()
        );

        when(outputPort.findCinemaByIdAdmin(adminId)).thenReturn(Optional.of(cinema));

        // Act
        CinemaDomainEntity result = useCase.findCinemaByIdAdmin(adminId);

        // Assert
        assertNotNull(result);
        assertEquals(cinema.getId(), result.getId());
        assertEquals(cinema.getName(), result.getName());
        verify(outputPort, times(1)).findCinemaByIdAdmin(adminId);
    }

    @Test
    void shouldThrowExceptionWhenCinemaNotFound() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        when(outputPort.findCinemaByIdAdmin(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class,
                () -> useCase.findCinemaByIdAdmin(adminId));

        assertEquals("Administrador sin Cine registrado", exception.getMessage());
        verify(outputPort, times(1)).findCinemaByIdAdmin(adminId);
    }
}

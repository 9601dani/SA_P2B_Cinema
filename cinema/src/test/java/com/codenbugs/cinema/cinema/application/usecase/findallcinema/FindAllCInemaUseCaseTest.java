package com.codenbugs.cinema.cinema.application.usecase.findallcinema;

import com.codenbugs.cinema.cinema.application.ports.output.FindingAllCinemaOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FindAllCInemaUseCaseTest {
    private FindingAllCinemaOutputPort findingAllCinemaOutputPort;
    private FindAllCinemaUseCase findAllCinemaUseCase;

    @BeforeEach
    void setUp() {
        findingAllCinemaOutputPort = mock(FindingAllCinemaOutputPort.class);
        findAllCinemaUseCase = new FindAllCinemaUseCase(findingAllCinemaOutputPort);
    }

    @Test
    void shouldReturnAllCinemas() {
        // Arrange
        CinemaDomainEntity cinema1 = new CinemaDomainEntity(
                UUID.randomUUID(),
                "CINE 1",
                "img1.png",
                "Address 1",
                UUID.randomUUID(),
                BigDecimal.valueOf(100),
                Instant.now()
        );

        CinemaDomainEntity cinema2 = new CinemaDomainEntity(
                UUID.randomUUID(),
                "CINE 2",
                "img2.png",
                "Address 2",
                UUID.randomUUID(),
                BigDecimal.valueOf(150),
                Instant.now()
        );

        List<CinemaDomainEntity> cinemas = Arrays.asList(cinema1, cinema2);
        when(findingAllCinemaOutputPort.findAllCinemas()).thenReturn(cinemas);

        // Act
        List<CinemaDomainEntity> result = findAllCinemaUseCase.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(cinema1));
        assertTrue(result.contains(cinema2));

        verify(findingAllCinemaOutputPort, times(1)).findAllCinemas();
    }

    @Test
    void shouldReturnEmptyListWhenNoCinemas() {
        // Arrange
        when(findingAllCinemaOutputPort.findAllCinemas()).thenReturn(Collections.emptyList());

        // Act
        List<CinemaDomainEntity> result = findAllCinemaUseCase.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(findingAllCinemaOutputPort, times(1)).findAllCinemas();
    }
}

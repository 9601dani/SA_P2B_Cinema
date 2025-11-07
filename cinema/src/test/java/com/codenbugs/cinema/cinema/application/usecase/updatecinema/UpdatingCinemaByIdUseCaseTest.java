package com.codenbugs.cinema.cinema.application.usecase.updatecinema;

import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdOutputPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByNameOutputPort;
import com.codenbugs.cinema.cinema.application.ports.output.UpdatingCinemaByIdOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdatingCinemaByIdUseCaseTest {
    @Mock
    private UpdatingCinemaByIdOutputPort updatingOutputPort;

    @Mock
    private FindingCinemaByNameOutputPort findingByNameOutputPort;

    @Mock
    private FindingCinemaByIdOutputPort findingByIdOutputPort;

    private UpdatingCinemaByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UpdatingCinemaByIdUseCase(updatingOutputPort, findingByNameOutputPort, findingByIdOutputPort);
    }

    @Test
    void shouldUpdateCinemaSuccessfully() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        CinemaDomainEntity existingCinema = new CinemaDomainEntity(
                cinemaId, "CINE ORIGINAL", "img.png", "Address 1",
                UUID.randomUUID(), BigDecimal.valueOf(100), Instant.now()
        );

        UpdateCinemaDto dto = new UpdateCinemaDto(
                "CINE NUEVO",
                "Address 2",
                existingCinema.getAdminUserId(),
                BigDecimal.valueOf(150),
                "img2.png"
        );

        when(findingByIdOutputPort.findCinemaById(cinemaId)).thenReturn(Optional.of(existingCinema));
        when(findingByNameOutputPort.findCinemaByName(dto.getName())).thenReturn(Optional.empty());

        when(updatingOutputPort.update(eq(cinemaId), any(CinemaDomainEntity.class)))
                .thenAnswer(invocation -> {
                    CinemaDomainEntity arg = invocation.getArgument(1);
                    // Devolver el objeto actualizado con ID y fecha simulada
                    return new CinemaDomainEntity(
                            cinemaId,
                            arg.getName(),
                            arg.getImageUrl(),
                            arg.getAddress(),
                            arg.getAdminUserId(),
                            arg.getDailyCost(),
                            Instant.now()
                    );
                });

        // Act
        CinemaDomainEntity result = useCase.updatingCinemaById(cinemaId, dto);

        // Assert
        assertNotNull(result);
        assertEquals(cinemaId, result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getDailyCost(), result.getDailyCost());
        assertEquals(dto.getAdminUserId(), result.getAdminUserId());

        verify(findingByIdOutputPort, times(1)).findCinemaById(cinemaId);
        verify(findingByNameOutputPort, times(1)).findCinemaByName(dto.getName());
        verify(updatingOutputPort, times(1)).update(eq(cinemaId), any(CinemaDomainEntity.class));
    }

    @Test
    void shouldThrowEntityNotFoundWhenCinemaDoesNotExist() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        UpdateCinemaDto dto = new UpdateCinemaDto("CINE NUEVO", "Address 2", UUID.randomUUID(), BigDecimal.valueOf(150), "img.png");

        when(findingByIdOutputPort.findCinemaById(cinemaId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class, () -> useCase.updatingCinemaById(cinemaId, dto));
        assertTrue(exception.getMessage().contains("Cinema no encontado para actualizar"));

        verify(findingByIdOutputPort, times(1)).findCinemaById(cinemaId);
        verifyNoInteractions(findingByNameOutputPort, updatingOutputPort);
    }

    @Test
    void shouldThrowEntityAlreadyExistsWhenNameIsDuplicated() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();
        CinemaDomainEntity existingCinema = new CinemaDomainEntity(
                cinemaId, "CINE ORIGINAL", "img.png", "Address 1",
                UUID.randomUUID(), BigDecimal.valueOf(100), Instant.now()
        );

        UpdateCinemaDto dto = new UpdateCinemaDto(
                "CINE DUPLICADO",
                "Address 2",
                existingCinema.getAdminUserId(),
                BigDecimal.valueOf(150),
                "img2.png"
        );

        CinemaDomainEntity anotherCinema = new CinemaDomainEntity(
                UUID.randomUUID(),
                dto.getName(),
                "imgX.png",
                "Address X",
                UUID.randomUUID(),
                BigDecimal.valueOf(120),
                Instant.now()
        );

        when(findingByIdOutputPort.findCinemaById(cinemaId)).thenReturn(Optional.of(existingCinema));
        when(findingByNameOutputPort.findCinemaByName(dto.getName())).thenReturn(Optional.of(anotherCinema));

        // Act & Assert
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> useCase.updatingCinemaById(cinemaId, dto));
        assertTrue(exception.getMessage().contains("Ya existe un cine con ese nombre"));

        verify(findingByIdOutputPort, times(1)).findCinemaById(cinemaId);
        verify(findingByNameOutputPort, times(1)).findCinemaByName(dto.getName());
        verifyNoInteractions(updatingOutputPort);
    }
}

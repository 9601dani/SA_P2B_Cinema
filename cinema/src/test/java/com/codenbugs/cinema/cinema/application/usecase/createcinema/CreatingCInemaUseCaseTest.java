package com.codenbugs.cinema.cinema.application.usecase.createcinema;

import com.codenbugs.cinema.cinema.application.ports.output.CinemaCreateWalletEventPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByNameOutputPort;
import com.codenbugs.cinema.cinema.application.ports.output.StoringCinemaOutputPort;
import com.codenbugs.cinema.cinema.application.usecase.createcinema.CreateCinemaDto;
import com.codenbugs.cinema.cinema.application.usecase.createcinema.CreatingCinemaUseCase;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreatingCInemaUseCaseTest {
    private StoringCinemaOutputPort storingCinemaOutputPort;
    private FindingCinemaByNameOutputPort findingCinemaByNameOutputPort;
    private CinemaCreateWalletEventPort cinemaCreateWalletEventPort;

    private CreatingCinemaUseCase creatingCinemaUseCase;

    @BeforeEach
    void setUp() {
        storingCinemaOutputPort = mock(StoringCinemaOutputPort.class);
        findingCinemaByNameOutputPort = mock(FindingCinemaByNameOutputPort.class);
        cinemaCreateWalletEventPort = mock(CinemaCreateWalletEventPort.class);

        creatingCinemaUseCase = new CreatingCinemaUseCase(
                storingCinemaOutputPort,
                findingCinemaByNameOutputPort,
                cinemaCreateWalletEventPort
        );
    }

    @Test
    void shouldCreateCinemaSuccessfully() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        CreateCinemaDto dto = new CreateCinemaDto(
                "CineTest",
                "Dirección 123",
                adminId,
                BigDecimal.valueOf(100),
                "imagen.png"
        );

        CinemaDomainEntity domainFromDto = dto.toDomain();

        when(findingCinemaByNameOutputPort.findCinemaByName(domainFromDto.getName()))
                .thenReturn(Optional.empty());

        CinemaDomainEntity savedCinema = new CinemaDomainEntity(
                UUID.randomUUID(),
                domainFromDto.getName(),
                domainFromDto.getImageUrl(),
                domainFromDto.getAddress(),
                domainFromDto.getAdminUserId(),
                domainFromDto.getDailyCost(),
                Instant.now()
        );

        // <- Cambiado aquí
        when(storingCinemaOutputPort.save(any(CinemaDomainEntity.class)))
                .thenReturn(savedCinema);

        // Act
        CinemaDomainEntity result = creatingCinemaUseCase.createCinema(dto);

        // Assert
        assertNotNull(result.getId());
        assertEquals(domainFromDto.getName(), result.getName());
        assertEquals(domainFromDto.getAddress(), result.getAddress());
        assertEquals(domainFromDto.getDailyCost(), result.getDailyCost());

        verify(cinemaCreateWalletEventPort).publishCinemaCreateWallet(result.getId());
        verify(storingCinemaOutputPort, times(1)).save(any(CinemaDomainEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenCinemaAlreadyExists() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        CreateCinemaDto dto = new CreateCinemaDto(
                "CineExistente",
                "Dirección 123",
                adminId,
                BigDecimal.valueOf(100),
                "imagen.png"
        );

        CinemaDomainEntity domainFromDto = dto.toDomain();

        when(findingCinemaByNameOutputPort.findCinemaByName(domainFromDto.getName()))
                .thenReturn(Optional.of(domainFromDto));

        // Act & Assert
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> creatingCinemaUseCase.createCinema(dto));

        assertEquals("Ya existe un cine con ese nombre", exception.getMessage());

        // save y publish no deberían ser llamados
        verifyNoInteractions(storingCinemaOutputPort);
        verifyNoInteractions(cinemaCreateWalletEventPort);
    }

    @Test
    void shouldFailWhenCinemaDtoHasInvalidName() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        CreateCinemaDto dto = new CreateCinemaDto(
                "A", // nombre inválido, menor a 3 letras
                "Dirección 123",
                adminId,
                BigDecimal.valueOf(100),
                "imagen.png"
        );

        // Act & Assert
        InvalidPropertyEntityDomain exception = assertThrows(InvalidPropertyEntityDomain.class,
                dto::toDomain);

        assertTrue(exception.getMessage().contains("Nombre del cine no valido"));
    }

    @Test
    void shouldFailWhenCinemaDtoHasInvalidDailyCost() {
        // Arrange
        UUID adminId = UUID.randomUUID();
        CreateCinemaDto dto = new CreateCinemaDto(
                "CineValido",
                "Dirección 123",
                adminId,
                BigDecimal.valueOf(0), // costo inválido
                "imagen.png"
        );

        // Act & Assert
        InvalidPropertyEntityDomain exception = assertThrows(InvalidPropertyEntityDomain.class,
                dto::toDomain);

        assertTrue(exception.getMessage().contains("Costo por dia del Cine, debe ser mayor a cero"));
    }
}

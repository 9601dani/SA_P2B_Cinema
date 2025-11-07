package com.codenbugs.cinema.showtime.application.usecase.createshowtime;

import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.output.FindMovieByIdOutputPort;
import com.codenbugs.cinema.showtime.application.ports.output.FindingShowTimeRangeDateByRoomIdOutputPort;
import com.codenbugs.cinema.showtime.application.ports.output.NotificationCreatedShowTimeEventPort;
import com.codenbugs.cinema.showtime.application.ports.output.StoringShowTimeOutputPort;
import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class CreatingShowTimeUseCaseTest {
    private StoringShowTimeOutputPort storingShowTimeOutputPort;
    private FindMovieByIdOutputPort findMovieByIdOutputPort;
    private FindingShowTimeRangeDateByRoomIdOutputPort findingShowTimeRangeDateByRoomIdOutputPort;
    private FindingRoomByIdOutputPort findingRoomByIdOutputPort;
    private NotificationCreatedShowTimeEventPort notificationCreatedShowTimeEventPort;

    private CreatingShowTimeUseCase useCase;

    private UUID movieId;
    private UUID roomId;
    private BigDecimal price;
    private LocalDate startDate;
    private LocalTime startTime;
    private CreateShowTimeCaseDto dto;

    @BeforeEach
    void setup() {
        storingShowTimeOutputPort = mock(StoringShowTimeOutputPort.class);
        findMovieByIdOutputPort = mock(FindMovieByIdOutputPort.class);
        findingShowTimeRangeDateByRoomIdOutputPort = mock(FindingShowTimeRangeDateByRoomIdOutputPort.class);
        findingRoomByIdOutputPort = mock(FindingRoomByIdOutputPort.class);
        notificationCreatedShowTimeEventPort = mock(NotificationCreatedShowTimeEventPort.class);

        useCase = new CreatingShowTimeUseCase(
                storingShowTimeOutputPort,
                findMovieByIdOutputPort,
                findingShowTimeRangeDateByRoomIdOutputPort,
                findingRoomByIdOutputPort,
                notificationCreatedShowTimeEventPort
        );

        movieId = UUID.randomUUID();
        roomId = UUID.randomUUID();
        price = BigDecimal.valueOf(10.5);
        startDate = LocalDate.now().plusDays(1);
        startTime = LocalTime.of(15, 0);
        dto = new CreateShowTimeCaseDto(movieId, price, roomId, startDate, startTime);
    }

    @Test
    void shouldCreateShowTimeSuccessfully() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        CreateShowTimeCaseDto dto = new CreateShowTimeCaseDto(
                movieId,
                BigDecimal.valueOf(10),
                roomId,
                LocalDate.now(),
                LocalTime.of(10, 0)
        );

        ShowTimeDomainEntity domain = dto.toDomain();

        when(findingRoomByIdOutputPort.findingRoomById(roomId))
                .thenReturn(Optional.of(mock(com.codenbugs.cinema.room.domain.RoomDomainEntity.class)));

        MovieDomainEntity movie = mock(MovieDomainEntity.class);
        when(movie.getDurationMinutes()).thenReturn(120);
        doNothing().when(movie).validateActive();
        when(findMovieByIdOutputPort.findById(movieId)).thenReturn(movie);
        when(findingShowTimeRangeDateByRoomIdOutputPort.existRegistersRangeDate(
                any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> useCase.creatShowTime(dto));

        verify(storingShowTimeOutputPort, times(1)).save(any(ShowTimeDomainEntity.class));
        verify(notificationCreatedShowTimeEventPort, times(1)).publisherNewShowtimeCustomers(any(ShowTimeDomainEntity.class));
    }

    @Test
    void shouldThrowEntityNotFoundWhenRoomDoesNotExist() {
        // Arrange
        when(findingRoomByIdOutputPort.findingRoomById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class, () -> useCase.creatShowTime(dto));
        assertEquals("No existe la sala para crear la funcion", exception.getMessage());
        verifyNoInteractions(storingShowTimeOutputPort);
        verifyNoInteractions(notificationCreatedShowTimeEventPort);
    }

    @Test
    void shouldThrowExceptionWhenMovieInactive() {
        // Arrange
        when(findingRoomByIdOutputPort.findingRoomById(roomId)).thenReturn(Optional.of(mock(com.codenbugs.cinema.room.domain.RoomDomainEntity.class)));
        MovieDomainEntity movie = mock(MovieDomainEntity.class);
        doThrow(new EntityNotFount("Movie inactive")).when(movie).validateActive();
        when(findMovieByIdOutputPort.findById(movieId)).thenReturn(movie);

        // Act & Assert
        assertThrows(EntityNotFount.class, () -> useCase.creatShowTime(dto));
        verifyNoInteractions(storingShowTimeOutputPort);
        verifyNoInteractions(notificationCreatedShowTimeEventPort);
    }

    @Test
    void shouldThrowExceptionWhenShowTimeOverlap() {
        UUID roomId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        CreateShowTimeCaseDto dto = new CreateShowTimeCaseDto(
                movieId, BigDecimal.TEN, roomId, LocalDate.now(), LocalTime.of(10, 0)
        );
        when(findingRoomByIdOutputPort.findingRoomById(roomId))
                .thenReturn(Optional.of(mock(RoomDomainEntity.class)));
        MovieDomainEntity movie = mock(MovieDomainEntity.class);
        when(movie.getDurationMinutes()).thenReturn(120);
        when(findMovieByIdOutputPort.findById(movieId)).thenReturn(movie);
        when(findingShowTimeRangeDateByRoomIdOutputPort.existRegistersRangeDate(
                eq(roomId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);
        // Act & Assert
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> useCase.creatShowTime(dto));

        assertEquals("La sala ya tiene una funcion en esa fecha y hora", exception.getMessage());
    }
}

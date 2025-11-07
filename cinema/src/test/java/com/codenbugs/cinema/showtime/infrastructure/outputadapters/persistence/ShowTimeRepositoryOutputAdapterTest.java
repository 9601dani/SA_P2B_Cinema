package com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.ShowTimeDbEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.mapper.ShowTimeRepositoryMapper;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.repository.ShowTimeDbEntityJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShowTimeRepositoryOutputAdapterTest {
    @Mock
    private ShowTimeRepositoryMapper mapper;

    @Mock
    private ShowTimeDbEntityJpaRepository jpaRepository;

    @InjectMocks
    private ShowTimeRepositoryOutputAdapter adapter;

    @Test
    void shouldSaveShowTime() {
        ShowTimeDomainEntity domain = mock(ShowTimeDomainEntity.class);
        ShowTimeDbEntity dbEntity = mock(ShowTimeDbEntity.class);
        when(mapper.timeDbEntity(domain)).thenReturn(dbEntity);

        adapter.save(domain);

        verify(mapper).timeDbEntity(domain);
        verify(jpaRepository).save(dbEntity);
    }

    @Test
    void shouldReturnTrueIfExistsRegistersInRange() {
        UUID roomId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        when(jpaRepository.existsByDateRangeAndRoomId(start, end, roomId)).thenReturn(true);

        boolean exists = adapter.existRegistersRangeDate(roomId, start, end);

        assertTrue(exists);
        verify(jpaRepository).existsByDateRangeAndRoomId(start, end, roomId);
    }

    @Test
    void shouldFindAllShowTimesByListRoomsId() {
        List<UUID> roomIds = List.of(UUID.randomUUID());
        ShowTimeDbEntity dbEntity = mock(ShowTimeDbEntity.class);
        ShowTimeDomainEntity domain = mock(ShowTimeDomainEntity.class);

        when(jpaRepository.findAllByRoomIdIn(roomIds)).thenReturn(List.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domain);

        List<ShowTimeDomainEntity> result = adapter.findAllShowTimesByListRoomsId(roomIds);

        assertEquals(1, result.size());
        assertEquals(domain, result.get(0));
    }

    @Test
    void shouldUpdateActive() {
        UUID id = UUID.randomUUID();
        ShowTimeDbEntity dbEntity = mock(ShowTimeDbEntity.class);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(dbEntity));

        adapter.updateActive(true, id);

        verify(dbEntity).setActive(true);
        verify(jpaRepository).save(dbEntity);
    }

    @Test
    void shouldThrowExceptionIfShowTimeNotFound() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFount ex = assertThrows(EntityNotFount.class,
                () -> adapter.updateActive(true, id));

        assertEquals("Funcion no encontrada para activar/desactivar", ex.getMessage());
    }

    @Test
    void shouldFindAllShowTimesByListRoomsIdRangeDate() {
        List<UUID> roomIds = List.of(UUID.randomUUID());
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(3);

        ShowTimeDbEntity dbEntity = mock(ShowTimeDbEntity.class);
        ShowTimeDomainEntity domain = mock(ShowTimeDomainEntity.class);

        when(jpaRepository.findAllByRoomIdInAndRangeDate(roomIds, start, end)).thenReturn(List.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domain);

        List<ShowTimeDomainEntity> result = adapter.findAllShowTimesByListRoomsIdRangeDate(roomIds, start, end);

        assertEquals(1, result.size());
        assertEquals(domain, result.get(0));
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        ShowTimeDbEntity dbEntity = mock(ShowTimeDbEntity.class);
        ShowTimeDomainEntity domain = mock(ShowTimeDomainEntity.class);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(dbEntity));
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domain);

        Optional<ShowTimeDomainEntity> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }
}

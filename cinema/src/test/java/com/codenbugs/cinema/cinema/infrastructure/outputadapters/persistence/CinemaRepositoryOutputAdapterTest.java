package com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.cinema.application.ports.output.*;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.entity.CinemaDbEntity;
import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.entity.mapper.CinemaRepositoryMapper;
import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.repository.CinemaDbEntityJpaRepository;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CinemaRepositoryOutputAdapterTest {
    @Mock
    private CinemaDbEntityJpaRepository jpaRepository;

    @Mock
    private CinemaRepositoryMapper mapper;

    @InjectMocks
    private CinemaRepositoryOutputAdapter adapter;

    private CinemaDomainEntity domain;
    private CinemaDbEntity dbEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID id = UUID.randomUUID();
        domain = new CinemaDomainEntity(
                id, "CINE TEST", "img.png", "Address 123",
                UUID.randomUUID(), BigDecimal.valueOf(100), Instant.now()
        );
        dbEntity = new CinemaDbEntity();
        dbEntity.setId(domain.getId());
        dbEntity.setName(domain.getName());
        dbEntity.setAddress(domain.getAddress());
        dbEntity.setImageUrl(domain.getImageUrl());
        dbEntity.setDailyCost(domain.getDailyCost());

        when(mapper.toDbEntity(domain)).thenReturn(dbEntity);
        when(mapper.toDomainEntity(dbEntity)).thenReturn(domain);
    }

    @Test
    void shouldSaveCinema() {
        when(jpaRepository.save(dbEntity)).thenReturn(dbEntity);

        CinemaDomainEntity result = adapter.save(domain);

        assertEquals(domain.getId(), result.getId());
        verify(jpaRepository, times(1)).save(dbEntity);
    }

    @Test
    void shouldFindCinemaByName() {
        when(jpaRepository.findByName(domain.getName())).thenReturn(Optional.of(dbEntity));

        Optional<CinemaDomainEntity> result = adapter.findCinemaByName(domain.getName());

        assertTrue(result.isPresent());
        assertEquals(domain.getName(), result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenCinemaByNameNotFound() {
        when(jpaRepository.findByName("NO_EXISTE")).thenReturn(Optional.empty());

        Optional<CinemaDomainEntity> result = adapter.findCinemaByName("NO_EXISTE");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindCinemaByAdminId() {
        when(jpaRepository.findByAdminUserId(domain.getAdminUserId())).thenReturn(Optional.of(dbEntity));

        Optional<CinemaDomainEntity> result = adapter.findCinemaByIdAdmin(domain.getAdminUserId());

        assertTrue(result.isPresent());
        assertEquals(domain.getAdminUserId(), result.get().getAdminUserId());
    }

    @Test
    void shouldUpdateCinema() {
        UUID cinemaId = domain.getId();
        when(jpaRepository.findById(cinemaId)).thenReturn(Optional.of(dbEntity));

        CinemaDomainEntity result = adapter.update(cinemaId, domain);

        assertEquals(domain.getName(), result.getName());
        verify(jpaRepository, times(1)).save(dbEntity);
    }

    @Test
    void shouldThrowWhenUpdateCinemaNotFound() {
        UUID cinemaId = UUID.randomUUID();
        when(jpaRepository.findById(cinemaId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFount.class, () -> adapter.update(cinemaId, domain));
    }

    @Test
    void shouldFindCinemaById() {
        when(jpaRepository.findById(domain.getId())).thenReturn(Optional.of(dbEntity));

        Optional<CinemaDomainEntity> result = adapter.findCinemaById(domain.getId());

        assertTrue(result.isPresent());
        assertEquals(domain.getId(), result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenCinemaByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<CinemaDomainEntity> result = adapter.findCinemaById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindAllCinemas() {
        List<CinemaDbEntity> dbList = List.of(dbEntity);
        when(jpaRepository.findAll()).thenReturn(dbList);

        List<CinemaDomainEntity> result = adapter.findAllCinemas();

        assertEquals(1, result.size());
        assertEquals(domain.getId(), result.get(0).getId());
    }
}

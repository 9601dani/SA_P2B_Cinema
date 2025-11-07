package com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.SeatDbEntity;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.mapper.SeatRepositoryMapper;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.repository.SeatDbEntityJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class SeatRepositoryOutputAdapterTest {
    @InjectMocks
    private SeatRepositoryOutputAdapter adapter;

    @Mock
    private SeatDbEntityJpaRepository repository;

    @Mock
    private SeatRepositoryMapper mapper;

    private UUID roomId;
    private UUID seatId;
    private SeatDomainEntity seatDomain;
    private SeatDbEntity seatDb;

    @BeforeEach
    void setup() {
        roomId = UUID.randomUUID();
        seatId = UUID.randomUUID();

        seatDomain = new SeatDomainEntity(1, 1, "A-01", roomId);
        seatDb = SeatDbEntity.builder()
                .id(seatId)
                .roomId(roomId)
                .rowNum(1)
                .colNum(1)
                .name("A-01")
                .build();
    }

    @Test
    void shouldSaveAllSeatsSuccessfully() {
        List<SeatDomainEntity> seats = List.of(seatDomain);
        List<SeatDbEntity> seatsDb = List.of(seatDb);

        when(mapper.toDbEntity(seatDomain)).thenReturn(seatDb);

        adapter.saveAll(seats);

        verify(mapper, times(1)).toDbEntity(seatDomain);
        verify(repository, times(1)).saveAll(seatsDb);
    }

    @Test
    void shouldFindAllSeatsByRoomIdSuccessfully() {
        List<SeatDbEntity> seatsDb = List.of(seatDb);
        List<SeatDomainEntity> seatsDomain = List.of(seatDomain);

        when(repository.findAllByRoomId(roomId)).thenReturn(seatsDb);
        when(mapper.toSeatDomainEntity(seatDb)).thenReturn(seatDomain);

        List<SeatDomainEntity> result = adapter.findAllSeatsByRoomId(roomId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(seatDomain, result.get(0));

        verify(repository, times(1)).findAllByRoomId(roomId);
        verify(mapper, times(1)).toSeatDomainEntity(seatDb);
    }

    @Test
    void shouldFindSeatByIdSuccessfully() {
        when(repository.findById(seatId)).thenReturn(Optional.of(seatDb));
        when(mapper.toSeatDomainEntity(seatDb)).thenReturn(seatDomain);

        Optional<SeatDomainEntity> result = adapter.findById(seatId);

        assertTrue(result.isPresent());
        assertEquals(seatDomain, result.get());

        verify(repository, times(1)).findById(seatId);
        verify(mapper, times(1)).toSeatDomainEntity(seatDb);
    }

    @Test
    void shouldReturnEmptyOptionalWhenSeatNotFound() {
        when(repository.findById(seatId)).thenReturn(Optional.empty());

        Optional<SeatDomainEntity> result = adapter.findById(seatId);

        assertTrue(result.isEmpty());
        verify(repository, times(1)).findById(seatId);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldFindAllSeatsByListRoomIdsSuccessfully() {
        List<UUID> roomIds = List.of(roomId);
        List<SeatDbEntity> seatsDb = List.of(seatDb);
        List<SeatDomainEntity> seatsDomain = List.of(seatDomain);

        when(repository.findAllByRoomIdIn(roomIds)).thenReturn(seatsDb);
        when(mapper.toSeatDomainEntity(seatDb)).thenReturn(seatDomain);

        List<SeatDomainEntity> result = adapter.findAllSeatsByListRoomIds(roomIds);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(seatDomain, result.get(0));

        verify(repository, times(1)).findAllByRoomIdIn(roomIds);
        verify(mapper, times(1)).toSeatDomainEntity(seatDb);
    }
}

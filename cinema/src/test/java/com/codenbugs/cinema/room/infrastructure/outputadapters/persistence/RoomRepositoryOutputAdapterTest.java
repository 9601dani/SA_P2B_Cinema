package com.codenbugs.cinema.room.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.RoomDbEntity;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.mapper.RoomRepositoryMapper;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.repository.RoomDbEntityJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class RoomRepositoryOutputAdapterTest {
    @InjectMocks
    private RoomRepositoryOutputAdapter adapter;

    @Mock
    private RoomRepositoryMapper mapper;

    @Mock
    private RoomDbEntityJpaRepository repository;

    private RoomDomainEntity domainRoom;
    private RoomDbEntity dbRoom;
    private UUID roomId;
    private UUID cinemaId;

    @BeforeEach
    void setup() {
        roomId = UUID.randomUUID();
        cinemaId = UUID.randomUUID();

        domainRoom = new RoomDomainEntity(
                roomId, cinemaId, 20,
                "img.png", "Sala Test", 4, 5, "Descripcion valida", true, false
        );

        dbRoom = new RoomDbEntity(); // suponiendo que tiene constructor vacío
        dbRoom.setId(roomId);
        dbRoom.setCinemaId(cinemaId);
        dbRoom.setCapacity(20);
        dbRoom.setName("Sala Test");
        dbRoom.setDescription("Descripcion valida");
        dbRoom.setImageUrl("img.png");
        dbRoom.setBlocked(false);
        dbRoom.setCommentsEnabled(true);
    }

    @Test
    void shouldSaveRoomSuccessfully() {
        // Arrange
        when(mapper.toDbEntity(domainRoom)).thenReturn(dbRoom);
        when(repository.save(dbRoom)).thenReturn(dbRoom);
        when(mapper.toDomainEntity(dbRoom)).thenReturn(domainRoom);

        // Act
        RoomDomainEntity result = adapter.save(domainRoom);

        // Assert
        assertNotNull(result);
        assertEquals(domainRoom, result);
        verify(repository).save(dbRoom);
    }

    @Test
    void shouldFindAllByCinemaIdSuccessfully() {
        // Arrange
        RoomDbEntity anotherDbRoom = new RoomDbEntity();
        anotherDbRoom.setId(UUID.randomUUID());
        anotherDbRoom.setCinemaId(cinemaId);

        when(repository.findAllByCinemaId(cinemaId)).thenReturn(List.of(dbRoom, anotherDbRoom));
        when(mapper.toDomainEntity(dbRoom)).thenReturn(domainRoom);
        when(mapper.toDomainEntity(anotherDbRoom)).thenReturn(new RoomDomainEntity(
                anotherDbRoom.getId(), cinemaId, null, null, null, null, null, null, false, false
        ));

        // Act
        List<RoomDomainEntity> results = adapter.findAllByCinemaId(cinemaId);

        // Assert
        assertEquals(2, results.size());
        verify(repository).findAllByCinemaId(cinemaId);
    }

    @Test
    void shouldUpdateRoomByIdSuccessfully() {
        // Arrange
        when(repository.findById(roomId)).thenReturn(Optional.of(dbRoom));
        when(repository.save(dbRoom)).thenReturn(dbRoom);
        when(mapper.toDomainEntity(dbRoom)).thenReturn(domainRoom);

        // Act
        RoomDomainEntity result = adapter.updatingRoomById(roomId, domainRoom);

        // Assert
        assertNotNull(result);
        assertEquals(domainRoom, result);
        verify(repository).findById(roomId);
        verify(repository).save(dbRoom);
    }

    @Test
    void updatingRoomByIdShouldThrowEntityNotFountWhenRoomDoesNotExist() {
        // Arrange
        when(repository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFount exception = assertThrows(EntityNotFount.class,
                () -> adapter.updatingRoomById(roomId, domainRoom));
        assertTrue(exception.getMessage().contains("Sala no encontrada"));
    }

    @Test
    void shouldFindRoomByIdSuccessfully() {
        // Arrange
        when(repository.findById(roomId)).thenReturn(Optional.of(dbRoom));
        when(mapper.toDomainEntity(dbRoom)).thenReturn(domainRoom);

        // Act
        Optional<RoomDomainEntity> result = adapter.findingRoomById(roomId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domainRoom, result.get());
    }

    @Test
    void findingRoomByIdShouldReturnEmptyWhenNotFound() {
        // Arrange
        when(repository.findById(roomId)).thenReturn(Optional.empty());

        // Act
        Optional<RoomDomainEntity> result = adapter.findingRoomById(roomId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldFindRoomByNameAndCinemaIdSuccessfully() {
        // Arrange
        String name = "Sala Test";
        when(repository.findByNameAndCinemaId(name, cinemaId)).thenReturn(Optional.of(dbRoom));
        when(mapper.toDomainEntity(dbRoom)).thenReturn(domainRoom);

        // Act
        Optional<RoomDomainEntity> result = adapter.findingRoomByNameAndCinemaId(name, cinemaId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(domainRoom, result.get());
    }

    @Test
    void findingRoomByNameAndCinemaIdShouldReturnEmptyWhenNotFound() {
        // Arrange
        String name = "Sala X";
        when(repository.findByNameAndCinemaId(name, cinemaId)).thenReturn(Optional.empty());

        // Act
        Optional<RoomDomainEntity> result = adapter.findingRoomByNameAndCinemaId(name, cinemaId);

        // Assert
        assertTrue(result.isEmpty());
    }
}

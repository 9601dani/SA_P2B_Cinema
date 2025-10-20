package com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.repository;

import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.ShowTimeDbEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShowTimeDbEntityJpaRepository extends JpaRepository<ShowTimeDbEntity, UUID> {

    @Query("""
        SELECT CASE WHEN COUNT(st) > 0 THEN true ELSE false END
        FROM showtime st
        WHERE st.roomId = :roomId
          AND st.startTime <= :endTime
          AND st.endTime >= :startTime
    """)
    boolean existsByDateRangeAndRoomId(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("roomId") UUID roomId
    );

    List<ShowTimeDbEntity> findAllByRoomIdIn(List<UUID> ids);
}

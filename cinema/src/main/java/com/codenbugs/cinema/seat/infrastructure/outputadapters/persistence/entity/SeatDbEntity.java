package com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity(name = "seat")
@Table(name = "seat", schema = "cinema", uniqueConstraints = {
@UniqueConstraint(
        name = "uk_seat_location",
        columnNames = {"room_id", "row_num", "col_num"}
)
    })
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class SeatDbEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID roomId;

    @NonNull
    @Column(nullable = false)
    private Integer rowNum;

    @NonNull
    @Column(nullable = false)
    private Integer colNum;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}

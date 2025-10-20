package com.codenbugs.cinema.showtimeSeat.infrastructure.outputadapters.persistence.entity;

import com.codenbugs.cinema.showtimeSeat.domain.ShowtimeSeatStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity(name = "showtimeseat")
@Table(name = "showtimeseat", schema = "cinema")
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class ShowTimeSeatDbEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID seatId;

    @NonNull
    @Column(name = "showtime_id", nullable = false)
    private UUID showTimeId;

    @NonNull
    @Column(nullable = false)
    private UUID customerId;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "STRING DEFAULT AVAILABLE")
    private ShowtimeSeatStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

}

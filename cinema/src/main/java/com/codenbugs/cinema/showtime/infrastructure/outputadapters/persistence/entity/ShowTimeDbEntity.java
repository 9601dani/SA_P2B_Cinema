package com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity(name = "showtime")
@Table(name = "showtime", schema = "cinema")
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class ShowTimeDbEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID roomId;

    @NonNull
    @Column(nullable = false)
    private UUID movieId;

    @NonNull
    @Column(nullable = false)
    private BigDecimal price;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime startTime;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime endTime;

    @NonNull
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;


}

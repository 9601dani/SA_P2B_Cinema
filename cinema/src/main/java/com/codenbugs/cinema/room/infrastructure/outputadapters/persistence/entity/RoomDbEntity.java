package com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity(name = "room")
@Table(name = "room", schema = "cinema")
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class RoomDbEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID cinemaId;

    @NonNull
    @Column(nullable = false)
    private Integer capacity;

    @NonNull
    @Column(nullable = false)
    private String imageUrl;

    @NonNull
    @Column(nullable = false)
    private String name;

    @NonNull
    @Column(nullable = false)
    private Integer rows;

    @NonNull
    @Column(nullable = false)
    private Integer columns;

    @NonNull
    @Column(nullable = false)
    private String description;

    @NonNull
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean commentsEnabled;

    @NonNull
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean blocked;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}

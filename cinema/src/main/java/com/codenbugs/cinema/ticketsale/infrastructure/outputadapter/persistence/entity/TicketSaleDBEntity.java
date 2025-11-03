package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity;

import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity(name = "ticket_sale")
@Table(name = "ticket_sale", schema = "sale")
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor(access = PRIVATE)
public class TicketSaleDBEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private UUID showtimeId;

    @NonNull
    @Column(nullable = false)
    private UUID seatId;

    @NonNull
    @Column(nullable = false)
    private UUID userId;

    @NonNull
    @Column(nullable = false)
    private Instant purchaseDate;

    @NonNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NonNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountPercentage;

    @NonNull
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceTotal;

    @NonNull
    @Column(nullable = false, columnDefinition = "sale.state_ticket")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private StateTicket state;

    @Column(nullable = true)
    private UUID promotionId;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}

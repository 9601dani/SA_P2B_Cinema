package com.codenbugs.cinema.room.domain;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DomainEntity
@Getter
public class RoomDomainEntity {
    private UUID id;
    private UUID cinemaId;
    private Integer capacity;
    private String imageUrl;
    private String name;
    private Integer rows;
    private Integer columns;
    private String description;
    private boolean commentsEnabled;
    private boolean blocked;

    public RoomDomainEntity(UUID id, UUID cinemaId, Integer capacity, String imageUrl, String name, Integer rows, Integer columns, String description, boolean commentsEnabled, boolean blocked) {
        this.id = id;
        this.cinemaId = cinemaId;
        this.capacity = capacity;
        this.imageUrl = imageUrl;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.description = description;
        this.commentsEnabled = commentsEnabled;
        this.blocked = blocked;
    }

    public RoomDomainEntity(UUID cinemaId, String imageUrl, String name, Integer rows, Integer columns, String description) {
        this.cinemaId = cinemaId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.description = description;
        this.validate();
    }

    private void validate() {
        if (id == null) {
            throw new InvalidPropertyEntityDomain("El id de cine no puede ser nulo");
        }

        if (this.rows == null || this.rows < 1) {
            throw new InvalidPropertyEntityDomain("El numero filas de asientos debe ser mayor a 0");
        }

        if (this.columns == null || this.columns < 1) {
            throw new InvalidPropertyEntityDomain("El numero columnas de asientos debe ser mayor a 0");
        }

        if (this.name == null || this.name.length() <= 3) {
            throw new InvalidPropertyEntityDomain("El nombre de la sala debe tener mas de 3 caracteres");
        }

        if (this.description == null || this.description.length() <= 10) {
            throw new InvalidPropertyEntityDomain("La descripcion de la sala debe tener mas de 10 caracteres");
        }

        if (rows > 50 || columns > 50) {
            throw new InvalidPropertyEntityDomain("Número de filas o columnas excede el límite permitido (50).");
        }
    }

    public void creationCalculations(){
        this.capacity = this.rows * this.columns;
        this.blocked = false;
        this.commentsEnabled = true;
    }

    /**
     * Genera todos los asientos para esta sala, basados en filas y columnas.
     */
    public List<SeatDomainEntity> generateSeats() {
        List<SeatDomainEntity> seats = new ArrayList<>();

        for (int row = 1; row <= rows; row++) {
            String rowCode = generateRowCode(row);
            for (int col = 1; col <= columns; col++) {
                String name = String.format("%s-%02d", rowCode, col);
                seats.add(new SeatDomainEntity(col, row, name, this.id));
            }
        }
        return seats;
    }

    private String generateRowCode(int rowNumber) {
        // Convierte 1 → A, 2 → B ... 27 → AA, 28 → AB, etc.
        StringBuilder code = new StringBuilder();
        while (rowNumber > 0) {
            rowNumber--;
            code.insert(0, (char) ('A' + (rowNumber % 26)));
            rowNumber /= 26;
        }
        return code.toString();
    }
}

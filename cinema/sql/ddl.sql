CREATE SCHEMA IF NOT EXISTS cinema;
CREATE SCHEMA IF NOT EXISTS "sale";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE cinema.cinema (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    image_url VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    admin_user_id UUID NOT NULL,
    daily_cost DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE cinema.room (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    cinema_id UUID NOT NULL,
    capacity INTEGER NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    rows INTEGER NOT NULL,
    columns INTEGER NOT NULL,
    description TEXT NOT NULL,
    comments_enabled BOOLEAN NOT NULL DEFAULT false,
    blocked BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_room_cinema FOREIGN KEY (cinema_id) REFERENCES cinema.cinema (id)
);

CREATE TABLE cinema.seat (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    room_id UUID NOT NULL,
    row_num INTEGER NOT NULL,
    col_num INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_seat_room FOREIGN KEY (room_id) REFERENCES cinema.room (id),
    CONSTRAINT uq_room_row_col UNIQUE (room_id, row_num, col_num)
);

ALTER TABLE cinema.seat ADD COLUMN name VARCHAR(5) NOT NULL;

CREATE TABLE cinema.showtime (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    room_id UUID NOT NULL,
    movie_id UUID NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_showtime_room FOREIGN KEY (room_id) REFERENCES cinema.room (id)
);

ALTER TABLE cinema.showtime ADD COLUMN price DECIMAL(10,2) NOT NULL;

CREATE TYPE sale.state_ticket AS ENUM (
   'PENDING_PAYMENT',
   'COMPLETED_PAYMENT',
   'REJECTED'
);

CREATE TABLE "sale"."ticket_sale" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    showtime_id UUID NOT NULL,
    seat_id UUID NOT NULL,
    user_id UUID NOT NULL,
    purchase_date TIMESTAMP NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    discount_percentage DECIMAL(10,2) NOT NULL,
    price_total DECIMAL(10,2) NOT NULL,
    state sale.state_ticket NOT NULL,
    promotion_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_ticket_seat FOREIGN KEY (seat_id) REFERENCES cinema.seat (id),
    CONSTRAINT fk_ticket_showtime FOREIGN KEY (showtime_id) REFERENCES cinema.showtime (id)
);
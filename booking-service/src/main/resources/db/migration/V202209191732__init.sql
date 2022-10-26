CREATE TABLE IF NOT EXISTS booking
(
    id           UUID        NOT NULL,
    status       VARCHAR(50) NOT NULL,
    client_id    UUID        NOT NULL,
    car_id       UUID        NOT NULL,
    car_owner_id UUID        NOT NULL,
    book_from    TIMESTAMP   NOT NULL,
    book_till    TIMESTAMP   NOT NULL,
    finish_time  TIMESTAMP,
    total_price  BIGINT      NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS booking_car_id_index ON booking (car_id);
CREATE INDEX IF NOT EXISTS booking_client_id_index ON booking (client_id);
CREATE INDEX IF NOT EXISTS booking_status_index ON booking (status);

COMMIT;
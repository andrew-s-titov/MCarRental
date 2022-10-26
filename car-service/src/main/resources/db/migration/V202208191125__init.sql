CREATE TABLE IF NOT EXISTS cars
(
    id              UUID         NOT NULL,
    visible         BOOLEAN DEFAULT TRUE,
    type            VARCHAR(50)  NOT NULL,
    fuel            VARCHAR(50)  NOT NULL,
    gear_box        VARCHAR(50)  NOT NULL,
    brand           VARCHAR(50)  NOT NULL,
    model           VARCHAR(100) NOT NULL,
    production_year INT          NOT NULL,
    engine_capacity DECIMAL      NOT NULL,
    number_of_seats INT          NOT NULL,
    layout          VARCHAR(100) NOT NULL,
    price_per_day   INT          NOT NULL,
    number_plate    VARCHAR(50)  NOT NULL,
    vin             varchar(50)  NOT NULL,
    owner_id        UUID         NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS cars_owner_id_index ON cars (owner_id);
CREATE INDEX IF NOT EXISTS cars_visibility_index ON cars (visible);

COMMIT;
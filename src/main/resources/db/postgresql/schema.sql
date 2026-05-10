DROP TABLE IF EXISTS apartments;
DROP TABLE IF EXISTS owners;

CREATE TABLE owners (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE apartments (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    rooms INTEGER NOT NULL CHECK (rooms > 0),
    area NUMERIC(8,2) NOT NULL CHECK (area > 0),
    floor INTEGER NOT NULL CHECK (floor >= 0),
    price_per_month NUMERIC(12,2) NOT NULL CHECK (price_per_month >= 0),
    furnished BOOLEAN NOT NULL DEFAULT FALSE,
    pets_allowed BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    owner_id BIGINT NOT NULL REFERENCES owners(id)
);

CREATE INDEX idx_apartments_city_lower ON apartments (LOWER(city));
CREATE INDEX idx_apartments_price ON apartments (price_per_month);
CREATE INDEX idx_apartments_owner_id ON apartments (owner_id);

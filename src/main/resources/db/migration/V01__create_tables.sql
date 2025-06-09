CREATE TABLE market (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    description TEXT NOT NULL,
    submission_time TIMESTAMP NOT NULL,
    photo_url TEXT
);

CREATE TABLE \"user\" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL
);

CREATE TABLE market (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    description TEXT NOT NULL,
    submission_time TIMESTAMP NOT NULL,
    photo_url TEXT,
    user_id INTEGER REFERENCES \"user\"(id) ON DELETE SET NULL
);
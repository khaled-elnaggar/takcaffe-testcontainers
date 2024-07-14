create table if not exists customers (
    id bigserial NOT NULL,
    name VARCHAR NOT NULL,
    registration_date DATE NOT NULL,
    primary key (id)
);

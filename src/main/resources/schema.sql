create table if not exists customers (
    id bigserial not null,
    name varchar not null,
    registration_date date not null,
    primary key (id)
);

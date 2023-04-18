create extension if not exists "uuid-ossp";

create table users(

    id uuid default uuid_generate_v4() primary key,
    name varchar(255) unique not null,
	email varchar(100) unique not null,
	password varchar(255) not null,
    created_at timestamp not null

);
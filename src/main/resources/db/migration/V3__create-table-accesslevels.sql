create extension if not exists "uuid-ossp";

create table accesslevels(

    id uuid default uuid_generate_v4() primary key,
    access_level varchar(100) unique not null

);
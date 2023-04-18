create table products(

    id uuid default uuid_generate_v4() primary key,
    name varchar(255) unique not null,
	price numeric(10,2) not null,
	category varchar(100) not null,
	description text not null,
    created_at timestamp not null,
	status varchar(100) not null

);
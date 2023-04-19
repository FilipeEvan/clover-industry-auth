/* Cria um novo banco de dados chamado clover-industry-auth-db */
CREATE DATABASE clover-industry-auth-db;

/* Adiciona uma extensão uuid-ossp que gera um valor UUID único para cada registro */
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

/* Cria uma tabela chamada users */
CREATE TABLE users(

    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL

);

/* Cria uma tabela chamada accesslevels */
CREATE TABLE accesslevels(

    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    access_level VARCHAR(100) UNIQUE NOT NULL

);

/* Cria uma tabela de associação chamada users_accesslevels entre a tabela users e a tabela accesslevels */
CREATE TABLE users_accesslevels(

    user_id UUID,
    accesslevel_id UUID,
    PRIMARY KEY(user_id, accesslevel_id),

    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE SET DEFAULT
    ON UPDATE SET DEFAULT,

    FOREIGN KEY (accesslevel_id)
    REFERENCES accesslevels (id)
    ON DELETE SET DEFAULT
    ON UPDATE SET DEFAULT

);

/* Cria uma tabela chamada products */
CREATE TABLE products(

    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
	price NUMERIC(10,2) NOT NULL,
	category VARCHAR(100) NOT NULL,
	description TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
	status VARCHAR(100) NOT NULL

);
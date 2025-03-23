--liquibase formatted sql
--changeset agolovanov:001
CREATE EXTENSION IF NOT EXISTS citext;
CREATE IF NOT EXISTS DOMAIN DOMAIN_EMAIL AS citext CHECK(VALUE ~ '^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$');

CREATE TABLE IF NOT EXISTS users(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(32) UNIQUE NOT NULL,
    email DOMAIN_EMAIL UNIQUE NOT NULL,
    password TEXT NOT NULL
);
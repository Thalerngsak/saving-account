CREATE DATABASE IF NOT EXISTS bankingdb;
USE bankingdb;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    citizen_id VARCHAR(255) NOT NULL UNIQUE,
    thai_name VARCHAR(255) NOT NULL,
    english_name VARCHAR(255) NOT NULL,
    pin_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    version BIGINT,
    account_number VARCHAR(7) NOT NULL UNIQUE,
    citizen_id VARCHAR(255) NOT NULL UNIQUE,
    thai_name VARCHAR(255) NOT NULL,
    english_name VARCHAR(255) NOT NULL,
    balance DECIMAL(19,2) NOT NULL
);

CREATE TABLE transactions (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    account_id  BIGINT      NOT NULL,
    `timestamp` DATETIME    NOT NULL,
    type        VARCHAR(50) NOT NULL,
    channel     VARCHAR(50) NOT NULL,
    amount      DECIMAL(19,2) NOT NULL,
    balance     DECIMAL(19,2) NOT NULL,
    remark      VARCHAR(255),
    PRIMARY KEY (id, `timestamp`)
)
ENGINE=InnoDB
PARTITION BY HASH( MONTH(`timestamp`) )
PARTITIONS 12;
# Tasks schema

# --- !Ups

CREATE SEQUENCE task_id_seq;
CREATE TABLE user (
    id long,
    email varchar(255)
);

CREATE TABLE item (
    id long,
    name varchar(255),
    price long
    );

CREATE TABLE cart (
    purchaseId long,
    itemId long,
    quantity int
    );

CREATE TABLE purchase (
    id  long,
    userId long,
    status  varchar(255)
);

# --- !Downs

DROP TABLE user;
DROP TABLE item;
DROP TABLE cart;
DROP TABLE purchase;
DROP SEQUENCE task_id_seq;
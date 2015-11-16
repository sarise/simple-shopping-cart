# Tasks schema

# --- !Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
    id long NOT NULL DEFAULT nextval('user_id_seq'),
    email varchar(255)
);

CREATE SEQUENCE item_id_seq;
CREATE TABLE item (
    id long NOT NULL DEFAULT nextval('item_id_seq'),
    name varchar(255),
    price long
    );

CREATE TABLE cart (
    purchaseId long,
    itemId long,
    quantity int
    );

CREATE SEQUENCE purchase_id_seq;
CREATE TABLE purchase (
    id long NOT NULL DEFAULT nextval('purchase_id_seq'),
    userId long,
    status  varchar(255)
);

# --- !Downs

DROP TABLE user;
DROP TABLE item;
DROP TABLE cart;
DROP TABLE purchase;
DROP SEQUENCE user_id_seq;
DROP SEQUENCE item_id_seq;
DROP SEQUENCE purchase_id_seq;
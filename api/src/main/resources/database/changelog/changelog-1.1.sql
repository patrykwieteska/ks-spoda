--liquibase formatted sql
--changeset ks-spoda:1.0

create TABLE "MATCHDAY" (
    "ID" NUMBER(10) AUTO_INCREMENT PRIMARY KEY NOT NULL,
    "DATE" DATE NOT NULL,
    "LEAGUE_ID" NUMBER(10) NOT NULL,
    "LOCATION" VARCHAR(100) NULL,
    "IS_FINISHED" NUMBER(1) NOT NULL,
    "CREATION_DATE" DATE NOT NULL,
    "LAST_MODIFICATION_DATE" DATE NOT NULL,
    "CREATED_BY" VARCHAR(50) NOT NULL,
    "IS_DELETED" NUMBER(1) NULL
);


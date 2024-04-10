--liquibase formatted sql
--changeset ks-spoda:1.0

create TABLE "MATCH_TEAM_PLAYER" (
    "ID" NUMBER(10) AUTO_INCREMENT PRIMARY KEY NOT NULL,
    "PLAYER_ID" NUMBER(10) NOT NULL,
    "MATCH_TEAM_ID" NUMBER(10) NOT NULL
);
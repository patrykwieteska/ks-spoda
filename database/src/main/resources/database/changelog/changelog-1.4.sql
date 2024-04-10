--liquibase formatted sql
--changeset ks-spoda:1.0

create TABLE MATCH_TEAM_PLAYER (
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    PLAYER_ID INT NOT NULL,
    MATCH_TEAM_ID INT NOT NULL
);
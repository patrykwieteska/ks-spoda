--liquibase formatted sql
--changeset ks-spoda:1.0
ALTER TABLE SEASON ADD COLUMN EURO_TOURNAMENT_ID VARCHAR(100) NULL;

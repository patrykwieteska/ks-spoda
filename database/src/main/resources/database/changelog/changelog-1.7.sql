--liquibase formatted sql
--changeset ks-spoda:1.0
ALTER TABLE MATCH_GAME ADD COLUMN EURO_MATCH_ID INT NULL;
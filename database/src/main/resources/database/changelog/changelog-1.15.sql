--liquibase formatted sql
--changeset ks-spoda:1.0

ALTER TABLE MATCH_GAME ADD COLUMN COMMENTARY VARCHAR(1000) NULL;
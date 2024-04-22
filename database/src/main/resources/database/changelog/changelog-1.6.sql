--liquibase formatted sql
--changeset ks-spoda:1.0
ALTER TABLE SEASON ADD COLUMN SEASON_NAME VARCHAR(50) NULL;
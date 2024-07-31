--liquibase formatted sql
--changeset ks-spoda:1.0

ALTER TABLE SEASON ADD COLUMN MATCH_WEIGHT_INDEX DECIMAL(10,2) NULL
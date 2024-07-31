--liquibase formatted sql
--changeset ks-spoda:1.0

ALTER TABLE LEAGUE ADD COLUMN START_DATE DATE NULL;
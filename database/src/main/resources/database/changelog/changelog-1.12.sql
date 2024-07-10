--liquibase formatted sql
--changeset ks-spoda:1.0

alter table MATCH_DAY_TABLE drop column CURRENT_POSITION;
alter table MATCH_DAY_TABLE drop column STANDBY_POSITION;
alter table MATCH_DAY_TABLE drop column PREVIOUS_POSITION;

alter table SEASON_TABLE drop column CURRENT_POSITION;
alter table SEASON_TABLE drop column STANDBY_POSITION;
alter table SEASON_TABLE drop column PREVIOUS_POSITION;

alter table LEAGUE_TABLE drop column CURRENT_POSITION;
alter table LEAGUE_TABLE drop column STANDBY_POSITION;
alter table LEAGUE_TABLE drop column PREVIOUS_POSITION;
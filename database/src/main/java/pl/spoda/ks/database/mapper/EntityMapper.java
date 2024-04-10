package pl.spoda.ks.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.entity.*;

@Mapper
public interface EntityMapper {

    LeagueDto mapToLeagueDto(League league);
    League mapToLeagueEntity(LeagueDto leagueDto);
    MatchDayDto mapToMatchDayDto(MatchDay matchDay);
    MatchDay mapToMatchDay(MatchDayDto matchDayDto);
    SeasonDto mapToSeasonDto(Season season);
    Season mapToSeason(SeasonDto seasonDto);
    @Mapping(target="joinDate", source="creationDate")
    PlayerDto mapToPlayerDto(Player player);
    Player mapToPlayer(PlayerDto playerDto);
    GameTeamDto mapToGameTeamDto(GameTeam gameTeam);
    GameTeam mapToGameTeam(GameTeamDto gameTeamDto);
    MatchTeamDto mapToMatchTeamDto(MatchTeam matchTeam);
    MatchTeam mapToMatchTeam(MatchTeamDto matchTeamDto);

    @Mapping( target="matchDayId", source = "matchDay.id")
    MatchDto mapToMatchDto(Match match);
    @Mapping( target="homeTeam", source = "homeTeam")
    @Mapping( target="awayTeam", source = "awayTeam")
    Match mapToMatch(MatchDto match);
    LeagueTableDto mapToLeagueTableDto(LeagueTable leagueTable);
    LeagueTable mapToLeagueTable(LeagueTableDto leaguePlayerDetailsDto);
    SeasonTableDto mapToSeasonTableDto(SeasonTable seasonTable);
    SeasonTable mapToSeasonTable(SeasonTableDto seasonPlayerDetailsDto);
    MatchDayTableDto mapToMatchDayTableDto(MatchDayTable matchDayTable);
    MatchDayTable mapToMatchDayTable(MatchDayTableDto matchDayTableDto);
    MatchDetailsDto mapToMatchDetailsDto(MatchDetails matchDetails);
    MatchDetails mapToMatchDetails(MatchDetailsDto matchDetailsDto);
    BaseDto mapToDto(BaseEntity baseEntity);
    BaseEntity mapToEntity(BaseDto baseDto);
}

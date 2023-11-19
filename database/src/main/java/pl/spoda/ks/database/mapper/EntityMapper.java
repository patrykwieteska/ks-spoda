package pl.spoda.ks.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.MatchDay;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.entity.Season;
import pl.spoda.ks.database.entity.table.LeagueTableRow;

@Mapper
public interface EntityMapper {

    @Mapping( target = "playerList", source = "players")
    @Mapping(target ="seasonList", source = "seasons")
    @Mapping( target = "leagueTableRowList", source = "leagueTableRowList")
    LeagueDto mapToLeagueDto(League league);
    League mapToLeagueEntity(LeagueDto leagueDto);
    MatchDayDto mapToMatchDayDto(MatchDay matchDay);
    MatchDay mapToMatchDay(MatchDayDto matchDayDto);
    SeasonDto mapToSeasonDto(Season season);
    Season mapToSeason(SeasonDto seasonDto);
    PlayerDto mapToPlayerDto(Player player);
    Player mapToPlayer(PlayerDto playerDto);
    LeagueTableRowDto mapToLeagueTableRowDto(LeagueTableRow leagueTableRow);
    LeagueTableRow mapToLeagueTableRow(LeagueTableRowDto leagueTableRowDto);
}

package pl.spoda.ks.database.mapper;

import org.mapstruct.Mapper;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.MatchDay;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.entity.Season;

@Mapper
public interface EntityMapper {

    LeagueDto mapToLeagueDto(League league);
    League mapToLeagueEntity(LeagueDto leagueDto);
    MatchDayDto mapToMatchDayDto(MatchDay matchDay);
    MatchDay mapToMatchDay(MatchDayDto matchDayDto);
    SeasonDto mapToSeasonDto(Season season);
    Season mapToSeason(SeasonDto seasonDto);
    PlayerDto mapToPlayerDto(Player player);
    Player mapToPlayer(PlayerDto playerDto);
}

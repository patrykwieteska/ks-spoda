package pl.spoda.ks.database.mapper;

import org.mapstruct.Mapper;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.MatchDay;

@Mapper
public interface EntityMapper {

    LeagueDto mapToLeagueDto(League league);
    League mapToLeagueEntity(LeagueDto leagueDto);
    MatchDayDto mapToMatchDayDto(MatchDay matchDay);
    MatchDay mapToMatchDay(MatchDayDto matchDayDto);
}

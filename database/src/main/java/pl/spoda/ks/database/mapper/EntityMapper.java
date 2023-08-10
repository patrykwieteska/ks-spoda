package pl.spoda.ks.database.mapper;

import org.mapstruct.Mapper;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.RoundDto;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.Round;

@Mapper
public interface EntityMapper {

    LeagueDto mapToLeagueDto(League league);
    League mapToLeagueEntity(LeagueDto leagueDto);
    RoundDto mapToRoundDto(Round round);
    Round mapToRound(RoundDto roundDto);
}

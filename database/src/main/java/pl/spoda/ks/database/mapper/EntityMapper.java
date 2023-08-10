package pl.spoda.ks.database.mapper;

import org.mapstruct.Mapper;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.entity.League;

@Mapper
public interface EntityMapper {

    LeagueDto mapToLeagueDto(League league);

    League mapToLeagueEntity(LeagueDto leagueDto);
}

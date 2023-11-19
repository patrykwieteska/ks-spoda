package pl.spoda.ks.api.season;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.model.LeagueData;
import pl.spoda.ks.api.season.model.init.InitSeasonResponse;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.SeasonDto;

@Service
@RequiredArgsConstructor
public class InitSeasonMapper {

    private final SeasonMapper seasonMapper;

    public InitSeasonResponse mapResponse(SeasonDto seasonDto, LeagueDto leagueDto) {
        return InitSeasonResponse.builder()
                .leagueData( LeagueData.builder()
                        .leagueId( leagueDto.getId() )
                        .name( leagueDto.getName() )
                        .description( leagueDto.getDescription() )
                        .teamStructure( TeamStructure.getByName( leagueDto.getTeamStructure() ) )
                        .type( LeagueType.getByName( leagueDto.getType() ) )
                        .build() )
                .seasonData(seasonMapper.mapSeason( seasonDto ))
                .build();
    }


}

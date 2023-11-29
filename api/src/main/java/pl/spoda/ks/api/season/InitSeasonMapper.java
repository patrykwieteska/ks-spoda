package pl.spoda.ks.api.season;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.model.LeagueData;
import pl.spoda.ks.api.matchday.MatchDayMapper;
import pl.spoda.ks.api.season.model.init.InitSeasonResponse;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.SeasonDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InitSeasonMapper {

    private final SeasonMapper seasonMapper;
    private final MatchDayMapper matchDayMapper;

    public InitSeasonResponse mapResponse(SeasonDto seasonDto, LeagueDto leagueDto, List<MatchDayDto> matchDayDtos) {
        return InitSeasonResponse.builder()
                .leagueData( LeagueData.builder()
                        .leagueId( leagueDto.getId() )
                        .name( leagueDto.getName() )
                        .description( leagueDto.getDescription() )
                        .teamStructure( TeamStructure.getByName( leagueDto.getTeamStructure() ) )
                        .type( LeagueType.getByName( leagueDto.getType() ) )
                        .build() )
                .seasonData( seasonMapper.mapSeason( seasonDto ) )
                .matchDayList( matchDayMapper.mapToSeasonMatchDayList( matchDayDtos ) )
                .build();
    }
}

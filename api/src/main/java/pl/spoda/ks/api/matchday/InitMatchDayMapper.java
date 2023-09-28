package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.model.MatchMapper;
import pl.spoda.ks.api.matchday.model.init.InitMatchDayResponse;
import pl.spoda.ks.api.matchday.model.init.MatchDayData;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.comons.utils.CollectionUtils;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.SeasonDto;

@Service
@RequiredArgsConstructor
public class InitMatchDayMapper {

    private final MatchMapper matchMapper;

    public InitMatchDayResponse mapToResponse(MatchDayDto matchDayDto, SeasonDto seasonDto, LeagueDto leagueDto) {
        return InitMatchDayResponse.builder()
                .matchDay( mapToMatchDayData( matchDayDto ) )
                .leagueName( leagueDto.getName() )
                .leagueDescription( leagueDto.getDescription() )
                .seasonEndDate( seasonDto.getEndDate() )
                .seasonStartDate( seasonDto.getStartDate() )
                .leagueType( LeagueType.getByName( leagueDto.getType() ) )
                .leagueTeamStructure( TeamStructure.getByName( leagueDto.getTeamStructure() ) )
                .build();
    }

    public MatchDayData mapToMatchDayData(MatchDayDto matchDay) {
        return MatchDayData.builder()
                .matchDayId( matchDay.getId() )
                .date( matchDay.getDate() )
                .isFinished( matchDay.getIsFinished() )
                .location( matchDay.getLocation() )
                .seasonMatchDay( matchDay.getSeasonMatchDay() )
                .matches( CollectionUtils.emptyIfNull( matchDay.getMatchList()).stream().map( matchMapper::mapToResponse ).toList() )
                .build();
    }
}

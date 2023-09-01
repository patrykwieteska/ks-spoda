package pl.spoda.ks.api.matchday;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.model.init.InitMatchDayResponse;
import pl.spoda.ks.api.matchday.model.init.MatchDayData;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.SeasonDto;

@Service
public class InitMatchDayMapper {
    public InitMatchDayResponse mapToResponse(MatchDayDto matchDayDto, SeasonDto seasonDto, LeagueDto leagueDto) {
        return InitMatchDayResponse.builder()
                .matchDay( mapToMatchDayData( matchDayDto ) )
                .leagueName( leagueDto.getName() )
                .leagueDescription( leagueDto.getDescription() )
                .seasonEndDate( seasonDto.getEndDate() )
                .seasonStartDate( seasonDto.getStartDate() )
                .build();
    }

    public MatchDayData mapToMatchDayData(MatchDayDto matchDay) {
        return MatchDayData.builder()
                .matchDayId( matchDay.getId() )
                .date( matchDay.getDate() )
                .isFinished( matchDay.getIsFinished() )
                .location( matchDay.getLocation() )
                .seasonMatchDay( matchDay.getSeasonMatchDay() )
                .build();
    }
}

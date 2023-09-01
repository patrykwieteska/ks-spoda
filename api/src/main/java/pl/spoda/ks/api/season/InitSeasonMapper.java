package pl.spoda.ks.api.season;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.InitMatchDayMapper;
import pl.spoda.ks.api.matchday.model.init.MatchDayData;
import pl.spoda.ks.api.season.model.init.InitSeasonResponse;
import pl.spoda.ks.api.season.model.init.SeasonData;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.SeasonDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InitSeasonMapper {

    private final InitMatchDayMapper initMatchDayMapper;

    public InitSeasonResponse mapResponse(SeasonDto seasonDto, List<MatchDayDto> seasonMatchDays, LeagueDto leagueDto) {


        return InitSeasonResponse.builder()
                .leagueId( leagueDto.getId() )
                .leagueName( leagueDto.getName() )
                .leagueDescription( leagueDto.getDescription() )
                .seasonData( SeasonData.builder()
                        .seasonId( seasonDto.getId() )
                        .startDate( seasonDto.getStartDate() )
                        .endDate( seasonDto.getEndDate() )
                        .isFinished( seasonDto.getIsFinished() )
                        .matchDays( mapMatchDays( seasonMatchDays ) )
                        .build() )
                .build();
    }

    private List<MatchDayData> mapMatchDays(List<MatchDayDto> seasonMatchDays) {
        return seasonMatchDays.stream()
                .map( initMatchDayMapper::mapToMatchDayData )
                .toList();
    }
}

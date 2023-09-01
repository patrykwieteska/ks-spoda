package pl.spoda.ks.api.season;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.model.SeasonRequest;
import pl.spoda.ks.comons.date.DateService;
import pl.spoda.ks.database.dto.SeasonDto;

@Service
@RequiredArgsConstructor
public class SeasonMapper {

    private final DateService dateService;

    public SeasonDto mapSeason(SeasonRequest request) {

        return SeasonDto.builder()
                .startDate( dateService.dateOf( request.getStartDate() ) )
                .leagueId( request.getLeagueId() )
                .isFinished( false )
                .build();
    }
}

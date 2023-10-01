package pl.spoda.ks.api.season;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.model.SeasonRequest;
import pl.spoda.ks.comons.date.DateService;
import pl.spoda.ks.database.dto.SeasonDto;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SeasonMapper {

    private final DateService dateService;

    @Value("${application.initial-rating}")
    public String initialRating;

    public SeasonDto mapSeason(SeasonRequest request) {
        return SeasonDto.builder()
                .startDate( dateService.dateOf( request.getStartDate() ) )
                .leagueId( request.getLeagueId() )
                .isFinished( false )
                .initialRating( new BigDecimal( initialRating ) )
                .build();
    }
}

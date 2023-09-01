package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.comons.date.DateService;
import pl.spoda.ks.database.dto.MatchDayDto;

@Service
@RequiredArgsConstructor
public class MatchDayMapper {

    private final DateService dateService;

    public MatchDayDto mapMatchDay(CreateMatchDayRequest request) {
        return MatchDayDto.builder()
                .date( dateService.dateOf( request.getMatchDayDate() ) )
                .seasonId( request.getSeasonId() )
                .location( request.getLocation() )
                .build();
    }
}

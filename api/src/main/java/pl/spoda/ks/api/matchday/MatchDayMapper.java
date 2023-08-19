package pl.spoda.ks.api.matchday;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.database.dto.MatchDayDto;

@Service
public class MatchDayMapper {
    public MatchDayDto mapMatchDay(CreateMatchDayRequest request) {
        return MatchDayDto.builder()
                .date( request.getMatchDayDate() )
                .leagueId( request.getLeagueId() )
                .location( request.getLocation() )
                .build();
    }
}

package pl.spoda.ks.api.league;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.database.dto.LeagueDto;

@Service
public class LeagueMapper {
    public LeagueDto mapLeague(LeagueRequest request) {

        return LeagueDto.builder()
                .startDate( request.getStartDate() )
                .name( request.getName() )
                .description( request.getDescription() )
                .isFinished( false )
                .build();
    }
}

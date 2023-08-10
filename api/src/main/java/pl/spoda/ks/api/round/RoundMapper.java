package pl.spoda.ks.api.round;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.round.model.CreateRoundRequest;
import pl.spoda.ks.database.dto.RoundDto;

@Service
public class RoundMapper {
    public RoundDto mapRound(CreateRoundRequest request) {
        return RoundDto.builder()
                .date( request.getRoundDate() )
                .leagueId( request.getLeagueId() )
                .build();
    }
}

package pl.spoda.ks.api.match.matchdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.enums.MatchResult;
import pl.spoda.ks.api.match.model.CreateMatchRequest;

@Service
@RequiredArgsConstructor
public class MatchResultService {

    public MatchResult getMatchResult(CreateMatchRequest request, Integer playerId) {
        if(request.getHomePlayers().contains( playerId )) {
            return getResult(request.getHomeGoals(), request.getAwayGoals());
        }

        return getResult( request.getAwayGoals(),request.getHomeGoals() );
    }

    public MatchResult getResult(Integer playerGoals, Integer opponentGoals) {
        if(playerGoals > opponentGoals)
            return MatchResult.WIN;
        return playerGoals < opponentGoals
                ? MatchResult.LOSE
                : MatchResult.DRAW;
    }


}

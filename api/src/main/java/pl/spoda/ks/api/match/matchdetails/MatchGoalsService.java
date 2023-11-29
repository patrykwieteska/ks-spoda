package pl.spoda.ks.api.match.matchdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.model.CreateMatchRequest;

@Service
@RequiredArgsConstructor
public class MatchGoalsService {

    public int getGoalsScored(CreateMatchRequest request, Integer playerId) {
        if(request.getHomePlayers().contains( playerId )) {
            return request.getHomeGoals();
        }
        return request.getAwayGoals();
    }

    public Integer getGoalsConceded(CreateMatchRequest request, Integer playerId) {
        if(request.getHomePlayers().contains( playerId )) {
            return request.getAwayGoals();
        }
        return request.getHomeGoals();
    }
}

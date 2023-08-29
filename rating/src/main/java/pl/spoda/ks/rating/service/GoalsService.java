package pl.spoda.ks.rating.service;

import org.springframework.stereotype.Service;
import pl.spoda.ks.rating.model.request.GameTeamData;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GoalsService {
    public BigDecimal calculateGoalsDifferenceIndex(GameTeamData teamA, GameTeamData teamB) {
        int goalsDifference = Math.abs( teamA.getGoals() - teamB.getGoals() );

        if (goalsDifference <= 1)
            return BigDecimal.ONE;

        if (goalsDifference == 2)
            return BigDecimal.valueOf( 1.5 );

        return (BigDecimal.valueOf( 11 ).add( BigDecimal.valueOf( goalsDifference ) ))
                .divide( BigDecimal.valueOf( 8 ),5, RoundingMode.CEILING );
    }
}

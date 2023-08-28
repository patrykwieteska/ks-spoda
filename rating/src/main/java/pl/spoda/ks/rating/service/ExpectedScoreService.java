package pl.spoda.ks.rating.service;

import org.springframework.stereotype.Service;
import pl.spoda.ks.rating.model.request.GameTeamData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;


@Service
public class ExpectedScoreService {

    private static final BigDecimal RATING_DIFFERENCE_INDEX = BigDecimal.valueOf( 400 );

    public Map<String, BigDecimal> calculateExpectedScores(
            GameTeamData teamA,
            GameTeamData teamB
    ) {
        Map<String,BigDecimal> expectedScores = new HashMap<>();

        BigDecimal teamRatingA = prepareTeamBaseRating( teamA );
        BigDecimal teamRatingB = prepareTeamBaseRating( teamB );

        double poweredGoalsA = Math.pow( 10, preparePower( teamRatingA, teamRatingB ) );
        BigDecimal expectedScoreTeamA =
                BigDecimal.ONE.divide( (BigDecimal.ONE.add( BigDecimal.valueOf( poweredGoalsA ) )), 5,RoundingMode.CEILING );
        BigDecimal expectedScoreTeamB = BigDecimal.ONE.subtract( expectedScoreTeamA );

        teamA.getPlayers().forEach( player -> expectedScores.put( player.getId(),expectedScoreTeamA ) );
        teamB.getPlayers().forEach( player -> expectedScores.put( player.getId(),expectedScoreTeamB ) );

        return expectedScores;
    }

    private static Double preparePower(BigDecimal teamRating, BigDecimal opponentRating) {
        return (opponentRating.subtract( teamRating ))
                .divide(  RATING_DIFFERENCE_INDEX ,
                        5,
                        RoundingMode.CEILING
                ).doubleValue();
    }

    private BigDecimal prepareTeamBaseRating(GameTeamData team) {
        int teamSize = team.getPlayers().size();
        return switch (teamSize) {
            case 1 -> team.getPlayers().get( 0 ).getRating();
            case 2 -> {
                BigDecimal player1Rating = team.getPlayers().get( 0 ).getRating();
                BigDecimal player2Rating = team.getPlayers().get( 1 ).getRating();
                yield (player1Rating.add( player2Rating ))
                        .divide(
                                new BigDecimal( teamSize ),
                                5,
                                RoundingMode.CEILING
                        );
            }
            default -> throw new RatingException( "Invalid team player list size" );
        };

    }
}

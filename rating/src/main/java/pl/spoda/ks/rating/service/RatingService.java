package pl.spoda.ks.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.rating.model.request.GamePlayerData;
import pl.spoda.ks.rating.model.request.GameTeamData;
import pl.spoda.ks.rating.model.request.RatingRequest;
import pl.spoda.ks.rating.model.response.RatingResponse;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final ExpectedScoreService expectedScoreService;
    private final ActualScoreService actualScoreService;
    private final GoalsService goalsService;

    private static final BigDecimal WEIGHT_INDEX = BigDecimal.valueOf( 20 );

    @LogEvent
    public RatingResponse calculateRating(@Valid RatingRequest request) {
        if (request.getTeamA() == null || request.getTeamB() == null)
            throw new RatingException( "Requested teams cannot be null" );

        GameTeamData teamA = request.getTeamA();
        GameTeamData teamB = request.getTeamB();

        Map<String, BigDecimal> actualScores = actualScoreService.calculateActualScores( teamA, teamB );
        Map<String, BigDecimal> expectedScores = expectedScoreService.calculateExpectedScores( teamA, teamB );
        BigDecimal goalsDifferenceIndex = goalsService.calculateGoalsDifferenceIndex( teamA, teamB );
        List<GamePlayerData> players = Stream.concat(
                request.getTeamA().getPlayers().stream(),
                request.getTeamB().getPlayers().stream()
        ).toList();

        return RatingResponse.builder()
                .players( prepareRatingMap( players, actualScores, expectedScores, goalsDifferenceIndex ) )
                .build();
    }

    private List<GamePlayerData> prepareRatingMap(
            List<GamePlayerData> players,
            Map<String, BigDecimal> actualScores,
            Map<String, BigDecimal> expectedScores,
            BigDecimal goalsDifferenceIndex
    ) {
        return players.stream()
                .map( player -> {
                    BigDecimal actualScore = actualScores.get( player.getId() );
                    BigDecimal expectedScore = expectedScores.get( player.getId() );

                    BigDecimal ratingDifference = calculateRatingDifference( actualScore, expectedScore,
                            goalsDifferenceIndex );

                    return GamePlayerData.builder()
                            .id( player.getId() )
                            .rating( prepareRating( player.getRating(), ratingDifference ) )
                            .difference( ratingDifference )
                            .build();
                } )
                .toList();
    }

    private BigDecimal prepareRating(
            BigDecimal playerRating,
            BigDecimal ratingDifference

    ) {
        return playerRating.add( ratingDifference );
    }

    private static BigDecimal calculateRatingDifference(BigDecimal actualScore, BigDecimal expectedScore, BigDecimal goalsDifferenceIndex) {
        return WEIGHT_INDEX
                .multiply( goalsDifferenceIndex )
                .multiply( actualScore.subtract( expectedScore ) )
                .divide( new BigDecimal( 1 ), 0,
                        RoundingMode.HALF_UP );
    }
}

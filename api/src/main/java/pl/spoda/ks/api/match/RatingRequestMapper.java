package pl.spoda.ks.api.match;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.rating.model.request.GamePlayerData;
import pl.spoda.ks.rating.model.request.GameTeamData;
import pl.spoda.ks.rating.model.request.RatingRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class RatingRequestMapper {
    public RatingRequest mapRequest(CreateMatchRequest createMatchRequest, String ratingType,
                                    Map<Integer, BigDecimal> playerCurrentRatingMap) {
        return RatingRequest.builder()
                .teamA( GameTeamData.builder()
                        .goals( createMatchRequest.getHomeGoals() )
                        .players( mapToGamePlayerDataList( createMatchRequest.getHomePlayers(), playerCurrentRatingMap ) )
                        .build() )
                .teamB(GameTeamData.builder()
                        .goals( createMatchRequest.getAwayGoals() )
                        .players( mapToGamePlayerDataList( createMatchRequest.getAwayPlayers(),playerCurrentRatingMap ) )
                        .build() )
                .mode( ratingType)
                .build();

    }

    private static List<GamePlayerData> mapToGamePlayerDataList(List<Integer> players, Map<Integer, BigDecimal> playerIdRatingMap) {
        return players.stream()
                .map( playerId -> mapToGamePlayerData( playerIdRatingMap, playerId ) )
                .toList();
    }

    private static GamePlayerData mapToGamePlayerData(Map<Integer, BigDecimal> playerIdRatingMap, Integer playerId) {
        return GamePlayerData.builder()
                .id( playerId.toString() )
                .rating( playerIdRatingMap.get( playerId ) )
                .build();
    }
}

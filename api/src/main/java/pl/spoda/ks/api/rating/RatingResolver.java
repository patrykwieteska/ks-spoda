package pl.spoda.ks.api.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.RatingRequestMapper;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.api.match.model.EditMatchRequest;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.rating.model.request.GamePlayerData;
import pl.spoda.ks.rating.model.request.GameTeamData;
import pl.spoda.ks.rating.model.request.RatingRequest;
import pl.spoda.ks.rating.model.response.RatingResponse;
import pl.spoda.ks.rating.service.RatingService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingResolver {

    private final RatingRequestMapper ratingRequestMapper;
    private final RatingService ratingService;


    public Map<Integer, BigDecimal> getRatingResponse(
            CreateMatchRequest createMatchRequest,
            String ratingType,
            List<MatchDetailsDto> newMatchDetailsList,
            Function<MatchDetailsDto, BigDecimal> function
    ) {
        RatingRequest request = ratingRequestMapper.mapRequest(
                createMatchRequest,
                ratingType,
                preparePlayerRatingMap( newMatchDetailsList, function )
        );

        RatingResponse response = ratingService.calculateRating( request );


        return response.getPlayers()
                .stream()
                .collect( Collectors.toMap(
                        player -> Integer.valueOf( player.getId() ),
                        GamePlayerData::getRating
                ) );
    }

    private Map<Integer, BigDecimal> preparePlayerRatingMap(
            List<MatchDetailsDto> newMatchDetailsList,
            Function<MatchDetailsDto, BigDecimal> function
    ) {
        return newMatchDetailsList.stream()
                .collect( Collectors.toMap(
                        MatchDetailsDto::getPlayerId,
                        function
                ) );
    }

    public Map<Integer, BigDecimal> getRatingResponseForMatchUpdate(
            EditMatchRequest request,
            String ratingType,
            List<Integer> homePlayers,
            List<Integer> awayPlayers,
            Map<Integer, BigDecimal> beforeRatingMap
    ) {
        RatingRequest ratingRequest = RatingRequest.builder()
                .mode( ratingType )
                .teamA( GameTeamData.builder()
                        .goals( request.getHomeGoals() )
                        .players(
                                homePlayers.stream()
                                        .map( player -> GamePlayerData.builder()
                                                .id( player.toString() )
                                                .rating( beforeRatingMap.get( player ) )
                                                .build() )
                                        .toList() )
                        .build() )
                .teamB( GameTeamData.builder()
                        .goals( request.getAwayGoals() )
                        .players(
                                awayPlayers.stream()
                                        .map( player -> GamePlayerData.builder()
                                                .id( player.toString() )
                                                .rating( beforeRatingMap.get( player ) )
                                                .build() )
                                        .toList() )
                        .build() )
                .build();

        RatingResponse ratingResponse = ratingService.calculateRating( ratingRequest );

        return ratingResponse.getPlayers()
                .stream()
                .collect( Collectors.toMap(
                        player -> Integer.valueOf( player.getId() ),
                        GamePlayerData::getRating
                ) );
    }
}

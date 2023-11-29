package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.RatingRequestMapper;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.dto.SeasonTableDto;
import pl.spoda.ks.database.service.SeasonTableServiceDB;
import pl.spoda.ks.rating.model.request.RatingRequest;
import pl.spoda.ks.rating.model.response.RatingResponse;
import pl.spoda.ks.rating.service.RatingService;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TableRatingService {

    private final RatingRequestMapper ratingRequestMapper;
    private final SeasonTableServiceDB seasonTableServiceDB;
    private final RatingService ratingService;

    @Value("${application.initial-rating}")
    public String initialRating;

    @LogEvent
    public RatingResponse getDataFromRatingService(
            CreateMatchRequest createMatchRequest,
            SeasonDto season,
            List<Integer> requestPlayerIds
    ) {
        Set<SeasonTableDto> seasonDetailsList = seasonTableServiceDB.getCurrentSeasonTable( season.getId() );
        Map<Integer, BigDecimal> playerIdRatingMap = preparePlayerRatingMap( requestPlayerIds, seasonDetailsList );
        RatingRequest request = ratingRequestMapper.mapRequest( createMatchRequest, season.getRatingType(),
                playerIdRatingMap );
        return ratingService.calculateRating( request );
    }

    private Map<Integer, BigDecimal> preparePlayerRatingMap(
            List<Integer> requestPlayerIds,
            Set<SeasonTableDto> seasonDetailsList
    ) {
        Map<Integer, BigDecimal> playerIdRatingMap = new HashMap<>();
        requestPlayerIds.forEach( playerId -> playerIdRatingMap.put(
                playerId,
                getRatingFromSeasonDetails( playerId, seasonDetailsList ).orElse( new BigDecimal( initialRating ) ) ) );
        return playerIdRatingMap;
    }

    private Optional<BigDecimal> getRatingFromSeasonDetails(
            Integer playerId,
            Set<SeasonTableDto> seasonDetails
    ) {
        return seasonDetails.stream()
                .filter( details -> details.getPlayer().getId().equals( playerId ) )
                .findFirst()
                .map( SeasonTableDto::getCurrentRating );
    }
}

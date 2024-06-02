package pl.spoda.ks.api.match.matchdetails;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.enums.MatchResult;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.api.match.model.EditMatchRequest;
import pl.spoda.ks.api.rating.RatingResolver;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.api.season.enums.RatingType;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.mapper.CloningMapper;
import pl.spoda.ks.database.service.MatchDetailsServiceDB;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchDetailsService {

    private final MatchDetailsServiceDB matchDetailsServiceDB;
    private final MatchGoalsService matchGoalsService;
    private final MatchResultService matchResultService;
    private final RatingResolver ratingResolver;
    private final CloningMapper cloningMapper = Mappers.getMapper( CloningMapper.class );

    @Value("${application.initial-rating}")
    public String initialRating;

    public List<MatchDetailsDto> getLatestMatchDetails(
            List<Integer> requestPlayerIds,
            Integer leagueId
    ) {
        return requestPlayerIds.stream()
                .map( playerId -> getPlayerDetails( playerId, leagueId ) )
                .filter( Objects::nonNull )
                .toList();
    }

    private MatchDetailsDto getPlayerDetails(Integer playerId, Integer leagueId) {
        return matchDetailsServiceDB.findLatestMatchDetails( playerId, leagueId );
    }

    public List<MatchDetailsDto> createNewMatchDetails(
            List<Integer> requestPlayerIds,
            CreateMatchRequest request,
            List<MatchDetailsDto> latestMatchDetails,
            SeasonDto season,
            Integer leagueId) {
        int seasonMatchesCount = matchDetailsServiceDB.getSeasonMatchesCount( season.getId() );
        int matchDayMatchesCount = matchDetailsServiceDB.getMatchDayMatchesCount( request.getMatchDayId() );
        PointCountingMethod pointCountingMethod = PointCountingMethod.getByName( season.getPointCountingMethod() );
        List<MatchDetailsDto> newMatchDetailsList = requestPlayerIds.stream()
                .map( playerId -> createMatchDetails( request, playerId, latestMatchDetails, pointCountingMethod,
                        leagueId, season.getId(),seasonMatchesCount,matchDayMatchesCount ) )
                .toList();


        updateByRatingData( request, newMatchDetailsList, season.getRatingType(), pointCountingMethod );

        return newMatchDetailsList;
    }

    private void updateByRatingData(CreateMatchRequest request, List<MatchDetailsDto> newMatchDetailsList, String ratingType, PointCountingMethod pointCountingMethod) {
        if (pointCountingMethod.equals( PointCountingMethod.RATING )) {
            Map<Integer, BigDecimal> seasonRatings = ratingResolver.getRatingResponse(
                    request,
                    ratingType,
                    newMatchDetailsList,
                    MatchDetailsDto::getSeasonRatingBeforeMatch );

            Map<Integer, BigDecimal> matchDayRatings = ratingResolver.getRatingResponse(
                    request,
                    ratingType,
                    newMatchDetailsList,
                    MatchDetailsDto::getMatchDayRatingBeforeMatch );

            newMatchDetailsList.forEach( details -> {
                details.setSeasonRatingAfterMatch( seasonRatings.get( details.getPlayerId() ) );
                details.setMatchDayRatingAfterMatch( matchDayRatings.get( details.getPlayerId() ) );
            } );
        }

        Map<Integer, BigDecimal> leagueRatings = ratingResolver.getRatingResponse(
                request,
                RatingType.SINGLE.name(),
                newMatchDetailsList,
                MatchDetailsDto::getLeagueRatingBeforeMatch );

        newMatchDetailsList.forEach( details ->
                details.setLeagueRatingAfterMatch( leagueRatings.get( details.getPlayerId() ) ) );
    }

    private MatchDetailsDto createMatchDetails(
            CreateMatchRequest request,
            Integer playerId,
            List<MatchDetailsDto> latestMatchDetails,
            PointCountingMethod pointCountingMethod,
            Integer leagueId,
            Integer seasonId,
            int seasonMatchesCount,
            int matchDayMatchesCount
    ) {
        Optional<MatchDetailsDto> lastPlayerMatchDetail =
                getLastPlayerMatchDetails( playerId, latestMatchDetails );
        MatchResult result = matchResultService.getMatchResult( request, playerId );
        return MatchDetailsDto.builder()
                .playerId( playerId )
                .leagueId( leagueId )
                .seasonId( seasonId )
                .matchDayId( request.getMatchDayId() )
                .goalsScored( matchGoalsService.getGoalsScored( request, playerId ) )
                .goalsConceded( matchGoalsService.getGoalsConceded( request, playerId ) )
                .matchPoints( result.getPoints() )
                .matchResult( result.name() )
                .matchInProgress( true )
                .leagueRatingBeforeMatch( lastPlayerMatchDetail.map( MatchDetailsDto::getLeagueRatingAfterMatch ).orElse( getInitialRating( PointCountingMethod.RATING ) ) )
                .seasonRatingBeforeMatch( getRatingBefore( pointCountingMethod, lastPlayerMatchDetail.orElse( null ), seasonMatchesCount,MatchDetailsDto::getSeasonRatingAfterMatch ) )
                .matchDayRatingBeforeMatch( getRatingBefore( pointCountingMethod, lastPlayerMatchDetail.orElse( null ),matchDayMatchesCount, MatchDetailsDto::getMatchDayRatingAfterMatch ) )
                .build();
    }

    private BigDecimal getRatingBefore(
            PointCountingMethod pointCountingMethod,
            MatchDetailsDto lastPlayerMatchDetails,
            int matchDayMatchesCount,
            Function<MatchDetailsDto,BigDecimal> getRatingAfterMatchFunction) {

        if(lastPlayerMatchDetails == null || matchDayMatchesCount==0) {
            return getInitialRating( pointCountingMethod );
        }
        return getRatingAfterMatchFunction.apply( lastPlayerMatchDetails );
    }

    private BigDecimal getInitialRating(PointCountingMethod pointCountingMethod) {
        return pointCountingMethod.equals( PointCountingMethod.RATING )
                ? new BigDecimal( initialRating )
                : null;
    }

    private Optional<MatchDetailsDto> getLastPlayerMatchDetails(Integer playerId, List<MatchDetailsDto> latestMatchDetails) {
        return latestMatchDetails.stream()
                .filter( details -> details.getPlayerId().equals( playerId ) )
                .findFirst();
    }

    public List<MatchDetailsDto> getUpdatedDetails(
            List<MatchDetailsDto> storedMatchDetailsList,
            EditMatchRequest request,
            MatchDto matchDto, MatchDayDto matchDayDto
    ) {
        List<MatchDetailsDto> updatedMatchDetails = new ArrayList<>();

        List<Integer> homePlayers = matchDto.getHomeTeam().getTeamPlayers().stream()
                .map( PlayerDto::getId ).toList();
        List<Integer> awayPlayers = matchDto.getAwayTeam().getTeamPlayers().stream()
                .map( PlayerDto::getId ).toList();

        SeasonDto season = matchDayDto.getSeason();
        PointCountingMethod pointCountingMethod = PointCountingMethod.getByName( season.getPointCountingMethod() );
        RatingType ratingType = RatingType.getByName( season.getRatingType() );

        Map<Integer, BigDecimal> beforeLeagueDayRating =
                getRatingBefore( storedMatchDetailsList, MatchDetailsDto::getLeagueRatingBeforeMatch );


        Map<Integer, BigDecimal> leagueRatings = ratingResolver.getRatingResponseForMatchUpdate(
                request,
                RatingType.SINGLE.name(),
                homePlayers,
                awayPlayers,
                beforeLeagueDayRating
        );
        Map<Integer, BigDecimal> seasonRatings = null;
        Map<Integer, BigDecimal> matchDayRatings = null;

        if (pointCountingMethod.equals( PointCountingMethod.RATING )) {
            Map<Integer, BigDecimal> beforeMatchDayRating =
                    getRatingBefore( storedMatchDetailsList, MatchDetailsDto::getMatchDayRatingBeforeMatch );
            Map<Integer, BigDecimal> beforeSeasonDayRating =
                    getRatingBefore( storedMatchDetailsList, MatchDetailsDto::getSeasonRatingBeforeMatch );

            seasonRatings = ratingResolver.getRatingResponseForMatchUpdate(
                    request,
                    ratingType.name(),
                    homePlayers,
                    awayPlayers,
                    beforeSeasonDayRating
            );

            matchDayRatings = ratingResolver.getRatingResponseForMatchUpdate(
                    request,
                    ratingType.name(),
                    homePlayers,
                    awayPlayers,
                    beforeMatchDayRating
            );
        }


        List<MatchDetailsDto> homeDetailsList = updateMatchDetails( storedMatchDetailsList, homePlayers,
                request.getHomeGoals(),
                request.getAwayGoals(), request,
                pointCountingMethod, leagueRatings, seasonRatings, matchDayRatings );
        List<MatchDetailsDto> awayDetailsList = updateMatchDetails( storedMatchDetailsList, awayPlayers,
                request.getAwayGoals(), request.getHomeGoals(), request,
                pointCountingMethod, leagueRatings, seasonRatings, matchDayRatings );

        updatedMatchDetails.addAll( homeDetailsList );
        updatedMatchDetails.addAll( awayDetailsList );
        return updatedMatchDetails;
    }

    private List<MatchDetailsDto> updateMatchDetails(
            List<MatchDetailsDto> storedMatchDetails,
            List<Integer> teamPlayers,
            Integer teamGoals,
            Integer opponentsGoals,
            EditMatchRequest editMatchRequest,
            PointCountingMethod pointCountingMethod,
            Map<Integer, BigDecimal> leagueRatings,
            Map<Integer, BigDecimal> seasonRatings,
            Map<Integer, BigDecimal> matchDayRatings
    ) {
        return storedMatchDetails.stream()
                .filter( details -> teamPlayers.contains( details.getPlayerId() ) )
                .map( details -> mapToNewDetailsDto( teamGoals, opponentsGoals, editMatchRequest, pointCountingMethod, leagueRatings, seasonRatings, matchDayRatings, details ) )
                .toList();
    }

    private MatchDetailsDto mapToNewDetailsDto(Integer teamGoals, Integer opponentsGoals, EditMatchRequest editMatchRequest, PointCountingMethod pointCountingMethod, Map<Integer, BigDecimal> leagueRatings, Map<Integer, BigDecimal> seasonRatings, Map<Integer, BigDecimal> matchDayRatings, MatchDetailsDto details) {
        MatchDetailsDto newDto = cloningMapper.clone( details );
        MatchResult matchResult = matchResultService.getResult( teamGoals, opponentsGoals );
        newDto.setGoalsScored( teamGoals );
        newDto.setGoalsConceded( opponentsGoals );
        newDto.setMatchResult( matchResult.name() );
        newDto.setMatchPoints( matchResult.getPoints() );
        newDto.setMatchInProgress( Optional.ofNullable( editMatchRequest.getIsComplete() ).map( value -> !value ).orElse( details.getMatchInProgress() ) );
        newDto.setMatchDayRatingAfterMatch( getRating( pointCountingMethod, matchDayRatings, details.getPlayerId() ) );
        newDto.setSeasonRatingAfterMatch( getRating( pointCountingMethod, seasonRatings, details.getPlayerId() ) );
        newDto.setLeagueRatingAfterMatch( getRating( PointCountingMethod.RATING, leagueRatings, details.getPlayerId() ) );
        return newDto;
    }

    private BigDecimal getRating(
            PointCountingMethod pointCountingMethod,
            Map<Integer, BigDecimal> ratingsMap,
            Integer playerId
    ) {
        return pointCountingMethod.equals( PointCountingMethod.RATING )
                ? ratingsMap.get( playerId )
                : null;
    }

    private Map<Integer, BigDecimal> getRatingBefore(
            List<MatchDetailsDto> storedMatchDetails,
            Function<MatchDetailsDto, BigDecimal> valueMapping
    ) {
        return storedMatchDetails.stream()
                .collect( Collectors.toMap( MatchDetailsDto::getPlayerId, valueMapping ) );
    }
}

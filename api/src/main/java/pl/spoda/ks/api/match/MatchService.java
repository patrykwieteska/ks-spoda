package pl.spoda.ks.api.match;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.match.matchdetails.MatchDetailsService;
import pl.spoda.ks.api.match.model.*;
import pl.spoda.ks.api.match.validator.MatchValidator;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.api.table.LeagueTableService;
import pl.spoda.ks.api.table.MatchDayTableService;
import pl.spoda.ks.api.table.SeasonTableService;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.service.MatchDayServiceDB;
import pl.spoda.ks.database.service.MatchDetailsServiceDB;
import pl.spoda.ks.database.service.MatchServiceDB;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchMapper matchMapper;
    private final MatchServiceDB matchServiceDB;
    private final ResponseResolver responseResolver;
    private final MatchDayServiceDB matchDayServiceDB;
    private final MatchDetailsService matchDetailsService;
    private final MatchValidator matchValidator;
    private final MatchDayTableService matchDayTableService;
    private final SeasonTableService seasonTableService;
    private final LeagueTableService leagueTableService;
    private final MatchDetailsServiceDB matchDetailsServiceDB;

    public ResponseEntity<BaseResponse> getMatchesByLeague(Integer leagueId) {
        List<MatchDto> leagueMatches = matchServiceDB.findMatchesByLeague( leagueId );
        List<Match> matches = matchMapper.mapToMatchList( leagueMatches );
        return responseResolver.prepareResponse( MatchListResponse.builder()
                .leagueId( leagueId )
                .matchList( matches )
                .build() );
    }

    public ResponseEntity<BaseResponse> createMatch(CreateMatchRequest request) {
        Integer matchDayId = request.getMatchDayId();
        MatchDayDto matchDay = matchDayServiceDB.getMatchDay( matchDayId );
        SeasonDto season = matchDay.getSeason();
        Integer leagueId = season.getLeagueId();
        List<Integer> requestPlayerIds = prepareMatchPlayerList( request );
        List<MatchDetailsDto> latestMatchDetails = matchDetailsService.getLatestMatchDetails( requestPlayerIds, leagueId );
        PointCountingMethod pointCountingMethod = PointCountingMethod.getByName( season.getPointCountingMethod() );

        matchValidator.validateCreate( request, requestPlayerIds, leagueId, latestMatchDetails, matchDay );

        List<MatchDetailsDto> newMatchDetails = matchDetailsService.createNewMatchDetails( requestPlayerIds, request, latestMatchDetails, season, leagueId );

        List<MatchDayTableDto> matchDayTable = matchDayTableService.getCurrentTable( matchDayId, newMatchDetails,
                pointCountingMethod );
        List<SeasonTableDto> seasonTable = seasonTableService.getCurrentTable( season.getId(), newMatchDetails,
                pointCountingMethod );
        List<LeagueTableDto> leagueTable = leagueTableService.getCurrentTable( leagueId, newMatchDetails,
                pointCountingMethod );

        MatchDto matchDto = matchMapper.mapToNewDto( request, matchDayTable );

        Integer newMatchId = matchServiceDB.saveMatch(
                matchDto,
                leagueTable,
                seasonTable,
                matchDayTable,
                newMatchDetails );

        return responseResolver.prepareResponseCreated( MatchCreated.builder().matchId( newMatchId ).build() );
    }

    private List<Integer> prepareMatchPlayerList(
            CreateMatchRequest createMatchRequest
    ) {
        List<Integer> requestedPlayers = new ArrayList<>();
        requestedPlayers.addAll( createMatchRequest.getHomePlayers() );
        requestedPlayers.addAll( createMatchRequest.getAwayPlayers() );
        return requestedPlayers;
    }

    public ResponseEntity<BaseResponse> editMatch(Integer matchId, EditMatchRequest request) {
        MatchDto matchDto = matchServiceDB.getMatchById( matchId );
        MatchDayDto matchDayDto = matchDayServiceDB.getMatchDay( matchDto.getMatchDayId() );

        matchValidator.validateEdit( matchDto, matchDayDto, request );
        List<MatchDetailsDto> findMatchDetailsList = matchDetailsServiceDB.findMatchDetailsList( matchId );
        List<MatchDetailsDto> storedMatchDetails = new ArrayList<>(findMatchDetailsList);
        List<MatchDetailsDto> updatedMatchDetailsList = matchDetailsService.getUpdatedDetails( storedMatchDetails,
                request, matchDto, matchDayDto );

        List<MatchDayTableDto> matchDayTable = matchDayTableService.updateTable( storedMatchDetails,
                updatedMatchDetailsList, matchDto, matchDayDto.getSeason().getPointCountingMethod() );

        List<SeasonTableDto> seasonTable = seasonTableService.updateTable( storedMatchDetails,
                updatedMatchDetailsList, matchDayDto );

        List<LeagueTableDto> leagueTable = leagueTableService.updateTable( storedMatchDetails,
                updatedMatchDetailsList, matchDayDto );

        MatchDto updatedMatch = matchMapper.updateMatch( matchDto, request );

        Integer updatedMatchId = matchServiceDB.saveMatch(
                updatedMatch,
                leagueTable,
                seasonTable,
                matchDayTable,
                updatedMatchDetailsList
        );

        return responseResolver.prepareResponseCreated( MatchCreated.builder().matchId( updatedMatchId ).build() );
    }

    public ResponseEntity<BaseResponse> removeMatch(Integer matchId) {
        MatchDto matchDto = matchServiceDB.getMatchById( matchId );
        List<MatchDetailsDto> storedMatchDetails = matchDetailsServiceDB.findMatchDetailsList( matchDto.getId() );
        Integer leagueId = storedMatchDetails.get( 0 ).getLeagueId();
        Integer seasonId = storedMatchDetails.get( 0 ).getSeasonId();
        Integer matchDayId = storedMatchDetails.get( 0 ).getMatchDayId();


        if (matchServiceDB.getNewestMatch( leagueId ) > matchId) {
            throw new SpodaApplicationException( InfoMessage.REMOVE_NEWEST_MATCHES );
        }

        List<MatchDayTableDto> matchDayTable = matchDayTableService.updateBeforeRemoveMatch( storedMatchDetails, matchDayId );
        List<SeasonTableDto> seasonTable = seasonTableService.updateBeforeRemoveMatch( storedMatchDetails, seasonId );
        List<LeagueTableDto> leagueTable = leagueTableService.updateBeforeRemoveMatch( storedMatchDetails, leagueId );

        matchServiceDB.removeMatch( matchDto.getId(), storedMatchDetails, matchDayTable, seasonTable, leagueTable );

        return responseResolver.prepareResponse( MatchCreated.builder().matchId( matchId ).build() );
    }
}

package pl.spoda.ks.api.match;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.match.matchdetails.MatchDetailsService;
import pl.spoda.ks.api.match.model.*;
import pl.spoda.ks.api.match.validator.MatchValidator;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.api.table.LeagueTableService;
import pl.spoda.ks.api.table.MatchDayTableService;
import pl.spoda.ks.api.table.SeasonTableService;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.service.*;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
    private final LeagueServiceDB leagueServiceDB;
    private final GameTeamServiceDB gameTeamServiceDB;
    private final EuroMatchService euroMatchService;
    private final MatchTeamsResolver matchTeamsResolver;

    public ResponseEntity<BaseResponse> getMatchesByLeague(Integer leagueId) {
        List<MatchDto> leagueMatches = matchServiceDB.findMatchesByLeague( leagueId );
        List<Match> matches = matchMapper.mapToMatchList( leagueMatches );
        return responseResolver.prepareResponse( MatchListResponse.builder()
                .matchList( matches )
                .build() );
    }

    @Transactional
    public ResponseEntity<BaseResponse> createMatch(CreateMatchRequest request) {
        Integer matchDayId = request.getMatchDayId();
        MatchDayDto matchDay = matchDayServiceDB.getMatchDay( matchDayId );
        Pair<Integer, Integer> matchTeams = matchTeamsResolver.prepareMatchTeams( request.getHomeGameTeamId(),
                request.getAwayGameTeamId()
                , request.getEuroMatchId(),
                matchDay.getSeason().getEuroTournamentId());

        request.setHomeGameTeamId( matchTeams.getFirst() );
        request.setAwayGameTeamId( matchTeams.getSecond() );


        SeasonDto season = matchDay.getSeason();
        LeagueDto league = leagueServiceDB.getSingleLeague( season.getLeagueId() );
        Integer leagueId = season.getLeagueId();
        List<Integer> requestPlayerIds = prepareMatchPlayerList( request );
        List<MatchDetailsDto> latestMatchDetails = matchDetailsService.getLatestMatchDetails( requestPlayerIds, leagueId );
        PointCountingMethod pointCountingMethod = PointCountingMethod.getByName( season.getPointCountingMethod() );
        TeamStructure teamStructure = TeamStructure.getByName( league.getTeamStructure() );
        matchValidator.validateCreate( request, requestPlayerIds, leagueId, latestMatchDetails, matchDay, teamStructure );

        List<MatchDetailsDto> newMatchDetails = matchDetailsService.createNewMatchDetails( requestPlayerIds, request, latestMatchDetails, season, leagueId );

        List<MatchDayTableDto> matchDayTable = matchDayTableService.getCurrentTable( matchDayId, newMatchDetails,
                PointCountingMethod.POINTS_TOTAL );
        List<SeasonTableDto> seasonTable = seasonTableService.getCurrentTable( season.getId(), newMatchDetails,
                pointCountingMethod );
        List<LeagueTableDto> leagueTable = leagueTableService.getCurrentTable( leagueId, newMatchDetails,
                PointCountingMethod.RATING );

        MatchDto matchDto = matchMapper.mapToNewDto( request, matchDayTable );

        Integer newMatchId = matchServiceDB.saveMatch(
                matchDto,
                leagueTable,
                seasonTable,
                matchDayTable,
                newMatchDetails );
        euroMatchService.updateEuroMatch( matchDto, season.getIsEuro(), false,season.getEuroTournamentId() );
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

    @Transactional
    public ResponseEntity<BaseResponse> editMatch(Integer matchId, EditMatchRequest request) {
        MatchDto matchDto = matchServiceDB.getMatchById( matchId );

        validateCompleteMatch( request, matchDto.getIsPlayOffMatch() );

        MatchDayDto matchDayDto = matchDayServiceDB.getMatchDay( matchDto.getMatchDayId() );
        Pair<Integer, Integer> matchTeams = matchTeamsResolver.prepareMatchTeams(
                matchDto.getHomeTeam().getGameTeamId(),
                matchDto.getAwayTeam().getGameTeamId(),
                request.getEuroMatchId(), matchDayDto.getSeason().getEuroTournamentId()
        );

        request.setHomeGameTeamId( matchTeams.getFirst() );
        request.setAwayGameTeamId( matchTeams.getSecond() );

        matchValidator.validateEdit( matchDto, matchDayDto, request );
        List<MatchDetailsDto> findMatchDetailsList = matchDetailsServiceDB.findMatchDetailsList( matchId );
        List<MatchDetailsDto> storedMatchDetails = new ArrayList<>( findMatchDetailsList );
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
        euroMatchService.updateEuroMatch( updatedMatch, matchDayDto.getSeason().getIsEuro(), request.getIsComplete(),
                matchDayDto.getSeason().getEuroTournamentId() );
        return responseResolver.prepareResponseCreated( MatchCreated.builder().matchId( updatedMatchId ).build() );
    }

    private void validateCompleteMatch(EditMatchRequest request, boolean playOffMatch) {
        if (BooleanUtils.isNotTrue( request.getIsComplete() )) {
            return;
        }

        boolean isDraw = request.getHomeGoals().equals( request.getAwayGoals() );

        if (isDraw && playOffMatch && (request.getPenalties() == null || request.getPenalties().getHomeGoals() == request.getAwayGoals())) {
            throw new SpodaApplicationException( "Nie można zakończyć meczu pucharowego remisem.\nPopraw wynik w " +
                    "rzutach karnych" );

        }
    }
    @Transactional
    public ResponseEntity<BaseResponse> removeMatch(Integer matchId) {
        MatchDto matchDto = matchServiceDB.getMatchById( matchId );
        List<MatchDetailsDto> storedMatchDetails = matchDetailsServiceDB.findMatchDetailsList( matchDto.getId() );
        Integer leagueId = storedMatchDetails.get( 0 ).getLeagueId();
        Integer seasonId = storedMatchDetails.get( 0 ).getSeasonId();
        Integer matchDayId = storedMatchDetails.get( 0 ).getMatchDayId();
        MatchDayDto matchDay = matchDayServiceDB.getMatchDay( matchDto.getMatchDayId() );

        if (matchServiceDB.getNewestMatch( leagueId ) > matchId) {
            throw new SpodaApplicationException( InfoMessage.REMOVE_NEWEST_MATCHES );
        }

        List<MatchDayTableDto> matchDayTable = matchDayTableService.updateBeforeRemoveMatch( storedMatchDetails, matchDayId );
        List<SeasonTableDto> seasonTable = seasonTableService.updateBeforeRemoveMatch( storedMatchDetails, seasonId );
        List<LeagueTableDto> leagueTable = leagueTableService.updateBeforeRemoveMatch( storedMatchDetails, leagueId );

        matchServiceDB.removeMatch( matchDto.getId(), storedMatchDetails, matchDayTable, seasonTable, leagueTable );
        euroMatchService.resetEuroMatch( matchDto.getEuroMatchId(),matchDay.getSeason().getEuroTournamentId() );
        return responseResolver.prepareResponse( MatchCreated.builder().matchId( matchId ).build() );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> getMatchesByMatchDay(Integer matchDayId) {
        List<MatchDto> matchDtoList = matchServiceDB.findMatchesByMatchDay( matchDayId );
        List<Match> matches = matchMapper.mapToMatchList( matchDtoList ).stream()
                .sorted( Comparator.comparing( Match::getId, Comparator.naturalOrder() ).reversed() ).toList();
        return responseResolver.prepareResponse( MatchListResponse.builder()
                .matchList( matches )
                .build() );
    }

    public ResponseEntity<BaseResponse> initGameTeams(Integer matchDayId) {
        List<GameTeamDto> gameTeams = gameTeamServiceDB.getGameTeams();
        List<Integer> usedGameTeams = gameTeamServiceDB.getUsedGameTeams( matchDayId );
        List<GameTeam> finalList = gameTeams.stream()
                .filter( gt -> !usedGameTeams.contains( gt.getId() ) )
                .map( matchMapper::mapToGameTeam )
                .sorted( Comparator.comparing( GameTeam::getName, Collator.getInstance( new Locale( "pl" ) ) ) )
                .toList();

        return responseResolver.prepareResponse( AvailableGameTeams.builder().gameTeams( finalList ).build() );
    }
}

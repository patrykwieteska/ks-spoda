package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.DeleteResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.matchday.model.*;
import pl.spoda.ks.api.matchday.model.init.InitMatchDayResponse;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.service.*;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchDayService {

    private final MatchDayServiceDB matchDayServiceDb;
    private final MatchServiceDB matchServiceDb;
    private final MatchDayValidator matchDayValidator;
    private final MatchDayMapper matchDayMapper;
    private final ResponseResolver responseResolver;
    private final InitMatchDayMapper initMatchDayMapper;
    private final SeasonServiceDB seasonServiceDb;
    private final LeagueServiceDB leagueServiceDb;
    private final MatchDayTableServiceDB matchDayTableServiceDB;
    private final MatchDayPlayerMapper matchDayPlayerMapper;

    @LogEvent
    public ResponseEntity<BaseResponse> createMatchDay(CreateMatchDayRequest request) {
        matchDayValidator.validateMatchDay( request );
        MatchDayDto matchDay = matchDayServiceDb.createMatchDay( matchDayMapper.mapMatchDay( request ) );
        return responseResolver.prepareResponseCreated( MatchDayStored.builder().matchDayId( matchDay.getId() ).build() );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> completeMatchDay(MatchDayStored request) {
        matchDayServiceDb.completeMatchDay( request.getMatchDayId() );
        return responseResolver.prepareResponse( request );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> initMatchDay(Integer matchDayId) {
        MatchDayDto matchDayDto = matchDayServiceDb.getMatchDay( matchDayId );
        SeasonDto seasonDto = seasonServiceDb.getSingleSeason( matchDayDto.getSeasonId() );
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague( seasonDto.getLeagueId() );
        InitMatchDayResponse response = initMatchDayMapper.mapToResponse( matchDayDto, seasonDto, leagueDto );
        return responseResolver.prepareResponse( response );
    }

    public ResponseEntity<BaseResponse> deleteMatchDay(Integer matchDayId) {
        matchDayServiceDb.deleteMatchDay( matchDayId );
        return responseResolver.prepareResponse( DeleteResponse.builder().deletedId( matchDayId ).build() );
    }

    public ResponseEntity<BaseResponse> getMatchDayList(Integer seasonId) {
        List<MatchDayDto> matchDaysBySeasonId = matchDayServiceDb.getMatchDaysBySeasonId( seasonId );
        List<SimpleMatchDay> simpleMatchDays = matchDayMapper.mapToSimpleMatchDayList( matchDaysBySeasonId );
        return responseResolver.prepareResponse( MatchDayList.builder().matchDays( simpleMatchDays ).build() );

    }

    @LogEvent
    public ResponseEntity<BaseResponse> getMatchDayPlayers(Integer matchDayId) {
        List<MatchDto> matchesByMatchDay = matchServiceDb.findMatchesByMatchDay( matchDayId );
        List<String> matchDayPlayers = matchDayTableServiceDB.getMatchDayTable( matchDayId ).stream()
                .map( table -> table.getPlayer().getAlias() )
                .toList();

        List<MatchDayPlayer> sortedPlayerList = matchDayPlayers.stream()
                .map( playerAlias -> mapToMatchDayPlayer( playerAlias, matchesByMatchDay, matchDayPlayers ) )
                .sorted( Comparator.comparing( MatchDayPlayer::getMatchesInRow ))
                .toList();
        MatchDayPlayersResponse response = MatchDayPlayersResponse.builder()
                .matchDayPlayers(
                        sortedPlayerList )
                .build();

        return responseResolver.prepareResponse( response );
    }

    private MatchDayPlayer mapToMatchDayPlayer(String playerAlias, List<MatchDto> matchesByMatchDay, List<String> matchDayPlayers) {
        return matchDayPlayerMapper.mapMatchDayPlayer(playerAlias,matchesByMatchDay,matchDayPlayers);

    }
}

package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.model.MatchDayPlayer;
import pl.spoda.ks.api.matchday.players.MatchInRowService;
import pl.spoda.ks.api.matchday.players.OpponentsService;
import pl.spoda.ks.api.matchday.players.TeammatesService;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.utils.CollectionUtils;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.service.MatchDayServiceDB;
import pl.spoda.ks.database.service.MatchServiceDB;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchDayPlayerMapper {

    private final OpponentsService opponentsService;
    private final TeammatesService teammatesService;
    private final MatchInRowService matchInRowService;
    private final MatchStatsService matchStatsService;
    private final MatchServiceDB matchServiceDB;
    private final MatchDayServiceDB matchDayServiceDB;

    public MatchDayPlayer mapMatchDayPlayer(String playerAlias, List<MatchDto> matchDayMatches, List<String> matchDayPlayers) {
        List<MatchDto> seasonMatches = CollectionUtils.emptyIfNull( matchDayMatches ).stream().limit( 1 )
                .map( match -> Optional.ofNullable( matchDayServiceDB.getMatchDay( match.getMatchDayId() ) )
                                .map( MatchDayDto::getSeasonId )
                                .orElseThrow( () -> new SpodaApplicationException( "Cannot find season for matchDay" ) ) )
                .map( matchServiceDB::findMatchesBySeason )
                .flatMap( Collection::stream )
                .distinct().toList();

        return MatchDayPlayer.builder()
                .alias( playerAlias )
                .matchesInRow( matchInRowService.getMatchesInRow( playerAlias, matchDayMatches ) )
                .opponents( opponentsService.getPlayerOpponents( playerAlias, matchDayMatches, matchDayPlayers ) )
                .teammates( teammatesService.getPlayerTeammates( playerAlias, matchDayMatches, matchDayPlayers ) )
//                .matchDayStats( matchStatsService.fillByGameStats( playerAlias, matchDayMatches ) )
                .seasonStats( matchStatsService.fillByGameStats( playerAlias, seasonMatches ) )
                .build();
    }
}

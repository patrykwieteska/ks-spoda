package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.api.table.model.TableResult;
import pl.spoda.ks.api.table.model.TableResultRow;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.service.*;

import java.text.Collator;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {

    private final MatchServiceDB matchServiceDB;
    private final TableRowsService tableRowsService;
    private final ResponseResolver responseResolver;
    private final LeagueTableServiceDB leagueTableServiceDB;
    private final SeasonTableServiceDB seasonTableServiceDB;
    private final SeasonServiceDB seasonServiceDB;
    private final PlayerServiceDB playerServiceDB;
    private final MatchDayTableServiceDB matchDayTableServiceDB;
    private final MatchDayServiceDB matchDayServiceDB;
    private final SortTableService sortTableService;

    public ResponseEntity<BaseResponse> getLeagueTable(Integer leagueId) {
        List<MatchDto> matchesByLeague = matchServiceDB.findMatchesByLeague( leagueId );
        Set<PlayerDto> leaguePlayer = new HashSet<>( playerServiceDB.getPlayerListByLeagueId( leagueId ) );
        Set<LeagueTableDto> leagueRatings = leagueTableServiceDB.getCurrentLeagueTable( leagueId );
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesByLeague, leaguePlayer, leagueRatings );
        List<TableResultRow> result = getSortedTableRows( tableRows );



        return prepareTableResponse( result, "Tabela ligowa", null );
    }

    private ResponseEntity<BaseResponse> prepareTableResponse(List<TableResultRow> result, String title,
                                                              PointCountingMethod pointCountingMethod) {
        return responseResolver
                .prepareResponse(
                        TableResult.builder()
                                .tableRows( result )
                                .pointCountingMethod( pointCountingMethod )
                                .header( title )
                                .build()
                );
    }

    private List<TableResultRow> getSortedTableRows(Set<TableResultRow> tableRows) {
        Function<TableResultRow, Integer> positionComparator = TableResultRow::getCurrentPosition;
        return tableRows.stream()
                .sorted( Comparator
                        .comparing( positionComparator, Comparator.naturalOrder() ).reversed()
                        .thenComparing( TableResultRow::getGoalsDiff )
                        .thenComparing( TableResultRow::getGoalsScored )
                        .thenComparing( TableResultRow::getGoalsConceded ).reversed()
                        .thenComparing( row -> row.getPlayer().getAlias(),
                                Collator.getInstance( new Locale( "pl" ) ) ) )
                .toList();
    }

    public ResponseEntity<BaseResponse> getSeasonTable(Integer seasonId) {
        List<MatchDto> matchesBySeason = matchServiceDB.findMatchesBySeason( seasonId );
        SeasonDto season = seasonServiceDB.getSingleSeason( seasonId );
        Set<SeasonTableDto> seasonPlayerDetails = seasonTableServiceDB.getCurrentSeasonTable( seasonId );
        Set<PlayerDto> seasonPlayers = seasonPlayerDetails.stream()
                .map( SeasonTableDto::getPlayer )
                .collect( Collectors.toSet() );
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesBySeason, seasonPlayers, seasonPlayerDetails );
        List<TableResultRow> result = getSortedTableRows( tableRows );

        return prepareTableResponse( result, null, PointCountingMethod.getByName( season.getPointCountingMethod() ) );
    }


    public ResponseEntity<BaseResponse> getMatchDayTable(Integer matchDayId) {
        List<MatchDto> matchesBySeason = matchServiceDB.findMatchesByMatchDay( matchDayId );
        Set<MatchDayTableDto> matchDayTableDtoSet = matchDayTableServiceDB.getCurrentMatchDayTable( matchDayId );
        Set<PlayerDto> matchDayPlayers = matchDayTableDtoSet.stream()
                .map( MatchDayTableDto::getPlayer )
                .collect( Collectors.toSet() );
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesBySeason, matchDayPlayers, matchDayTableDtoSet );
        List<TableResultRow> result = getSortedTableRows( tableRows );
        return prepareTableResponse( result, null, PointCountingMethod.POINTS_TOTAL );
    }
}

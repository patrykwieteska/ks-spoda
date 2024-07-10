package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.api.table.enums.PreviousPositionReference;
import pl.spoda.ks.api.table.model.TableResult;
import pl.spoda.ks.api.table.model.TableResultRow;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.service.*;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {

    /*
     * Podczas zapisywania tabel nie zapisujemy pozycji gracza w tabeli.
     * Podczas odczytywania tabel sortujemy i generujemy pozycje w tabeli.
     */

    private final MatchServiceDB matchServiceDB;
    private final TableRowsService tableRowsService;
    private final ResponseResolver responseResolver;
    private final LeagueTableServiceDB leagueTableServiceDB;
    private final SeasonTableServiceDB seasonTableServiceDB;
    private final SeasonServiceDB seasonServiceDB;
    private final PlayerServiceDB playerServiceDB;
    private final MatchDayTableServiceDB matchDayTableServiceDB;

    public ResponseEntity<BaseResponse> getLeagueTable(Integer leagueId) {
        List<MatchDto> matchesByLeague = matchServiceDB.findMatchesByLeague( leagueId );
        Set<PlayerDto> leaguePlayer = new HashSet<>( playerServiceDB.getPlayerListByLeagueId( leagueId ) );
        Set<LeagueTableDto> leagueRatings = leagueTableServiceDB.getCurrentLeagueTable( leagueId );
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesByLeague, leaguePlayer, leagueRatings );
        List<TableResultRow> result = getSortedTableRows( tableRows, PointCountingMethod.RATING );
        return prepareTableResponse( result, "Tabela ligowa", null );
    }

    private ResponseEntity<BaseResponse> prepareTableResponse(
            List<TableResultRow> tableRows,
            String title,
            PointCountingMethod pointCountingMethod
    ) {
        return responseResolver.prepareResponse(
                TableResult.builder()
                        .tableRows( tableRows )
                        .pointCountingMethod( pointCountingMethod )
                        .header( title )
                        .build()
        );
    }

    private List<TableResultRow> getSortedTableRows(Set<TableResultRow> tableRows, PointCountingMethod pointCountingMethod) {
        Function<TableResultRow, BigDecimal> positionComparator = preparePositionComparator( pointCountingMethod );
        List<TableResultRow> sortedRows = tableRows.stream()
                .sorted( Comparator
                        .comparing( positionComparator, Comparator.naturalOrder() )
                        .thenComparing( TableResultRow::getGoalsDiff )
                        .thenComparing( TableResultRow::getGoalsScored )
                        .thenComparing( TableResultRow::getGoalsConceded ).reversed()
                        .thenComparing( row -> row.getPlayer().getAlias(),
                                Collator.getInstance( new Locale( "pl" ) ) ) )
                .toList();

        Map<Integer, Integer> playerPreviousRatingPosition = preparePreviousPlayerPositions( tableRows );

        for (int i = 0; i < sortedRows.size(); i++) {
            updatePlayerPosition( sortedRows.get( i ), playerPreviousRatingPosition, i );
        }


        return sortedRows;

    }

    private void updatePlayerPosition(
            TableResultRow tableResultRow,
            Map<Integer, Integer> playerPreviousRatingPosition,
            int iterator
    ) {
        Integer currentPosition = iterator + 1;
        Integer previousPosition = playerPreviousRatingPosition.get( tableResultRow.getPlayer().getId() );

        tableResultRow.setCurrentPosition( currentPosition );
        tableResultRow.setPreviousPosition( previousPosition );
        tableResultRow.setPreviousPositionReference(
                preparePreviousPositionReference( tableResultRow.getRating(), tableResultRow.getPreviousRating() ).getValue()
        );
    }

    private PreviousPositionReference preparePreviousPositionReference(BigDecimal currentRating, BigDecimal previousRating) {


        if (currentRating.compareTo( previousRating ) == 0)
            return PreviousPositionReference.NONE;

        return currentRating.compareTo( previousRating ) > 0
                ? PreviousPositionReference.DOWN
                : PreviousPositionReference.UP;
    }

    private Map<Integer, Integer> preparePreviousPlayerPositions(Set<TableResultRow> tableRows) {
        Map<Integer, Integer> playerIdPreviousRatingPositions = new HashMap<>();
        List<TableResultRow> list = tableRows.stream()
                .sorted( Comparator.comparing( TableResultRow::getPreviousRating, Comparator.naturalOrder() ) )
                .toList();
        for (int i = 0; i < list.size(); i++) {
            playerIdPreviousRatingPositions.put( list.get( i ).getPlayer().getId(), i + 1 );
        }
        return playerIdPreviousRatingPositions;
    }

    public ResponseEntity<BaseResponse> getSeasonTable(Integer seasonId) {
        List<MatchDto> matchesBySeason = matchServiceDB.findMatchesBySeason( seasonId );
        SeasonDto season = seasonServiceDB.getSingleSeason( seasonId );
        Set<SeasonTableDto> seasonPlayerDetails = seasonTableServiceDB.getCurrentSeasonTable( seasonId );
        Set<PlayerDto> seasonPlayers = seasonPlayerDetails.stream()
                .map( SeasonTableDto::getPlayer )
                .collect( Collectors.toSet() );
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesBySeason, seasonPlayers, seasonPlayerDetails );
        List<TableResultRow> result = getSortedTableRows( tableRows, PointCountingMethod.getByName( season.getPointCountingMethod() ) );

        return prepareTableResponse( result, null, PointCountingMethod.getByName( season.getPointCountingMethod() ) );
    }


    public ResponseEntity<BaseResponse> getMatchDayTable(Integer matchDayId) {
        List<MatchDto> matchesBySeason = matchServiceDB.findMatchesByMatchDay( matchDayId );
        Set<MatchDayTableDto> matchDayTableDtoSet = matchDayTableServiceDB.getCurrentMatchDayTable( matchDayId );
        Set<PlayerDto> matchDayPlayers = matchDayTableDtoSet.stream()
                .map( MatchDayTableDto::getPlayer )
                .collect( Collectors.toSet() );
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesBySeason, matchDayPlayers, matchDayTableDtoSet );
        List<TableResultRow> result = getSortedTableRows( tableRows, PointCountingMethod.RATING );
        return prepareTableResponse( result, null, PointCountingMethod.RATING );
    }

    private Function<TableResultRow, BigDecimal> preparePositionComparator(PointCountingMethod countingMethod) {
        return switch (countingMethod) {
            case RATING -> TableResultRow::getRating;
            case POINTS_TOTAL -> TableResultRow::getPointsTotal;
            case POINTS_PER_MATCH -> TableResultRow::getPointsPerMatch;
        };
    }
}

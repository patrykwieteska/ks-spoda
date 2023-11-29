package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.table.model.TableResult;
import pl.spoda.ks.api.table.model.TableResultRow;
import pl.spoda.ks.database.dto.LeagueTableDto;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.database.dto.SeasonTableDto;
import pl.spoda.ks.database.service.LeagueServiceDB;
import pl.spoda.ks.database.service.LeagueTableServiceDB;
import pl.spoda.ks.database.service.MatchServiceDB;
import pl.spoda.ks.database.service.SeasonTableServiceDB;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {

    private final MatchServiceDB matchServiceDB;
    private final TableRowsService tableRowsService;
    private final LeagueServiceDB leagueServiceDB;
    private final ResponseResolver responseResolver;
    private final LeagueTableServiceDB leagueTableServiceDB;
    private final SeasonTableServiceDB seasonTableServiceDB;

    public ResponseEntity<BaseResponse> getLeagueTable(Integer leagueId) {
        List<MatchDto> matchesByLeague = matchServiceDB.findMatchesByLeague( leagueId );
        Set<PlayerDto> leaguePlayer = leagueServiceDB.getSingleLeague( leagueId ).getPlayerList();
        Set<LeagueTableDto> leagueRatings = leagueTableServiceDB.getCurrentLeagueTable( leagueId );
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesByLeague, leaguePlayer,leagueRatings);
        List<TableResultRow> result = getSortedTableRows( tableRows );
        return prepareTableResponse( result, "Tabela ligowa"  );
    }

    private ResponseEntity<BaseResponse> prepareTableResponse(List<TableResultRow> result, String title) {
        return responseResolver
                .prepareResponse(
                        TableResult.builder()
                                .tableRows( result )
                                .header( title )
                                .build()
                );
    }

    private List<TableResultRow> getSortedTableRows(Set<TableResultRow> tableRows) {
        Function<TableResultRow, Integer> positionComparator = TableResultRow::getCurrentPosition;
        return tableRows.stream()
                .sorted( Comparator
                        .comparing( positionComparator,Comparator.naturalOrder() ).reversed()
                        .thenComparing( TableResultRow::getGoalsDiff )
                        .thenComparing( TableResultRow::getGoalsScored )
                        .thenComparing( TableResultRow::getGoalsConceded ).reversed()
                        .thenComparing( row -> row.getPlayer().getAlias(),
                                Collator.getInstance( new Locale( "pl" ) ) ) )
                .toList();
    }

    public ResponseEntity<BaseResponse> getSeasonTable(Integer seasonId) {
        List<MatchDto> matchesBySeason = matchServiceDB.findMatchesBySeason( seasonId );
        Set<SeasonTableDto> seasonPlayerDetails = seasonTableServiceDB.getCurrentSeasonTable( seasonId );
        Set<PlayerDto> seasonPlayers = seasonPlayerDetails.stream()
                .map( SeasonTableDto::getPlayer )
                .collect( Collectors.toSet());
        Set<TableResultRow> tableRows = tableRowsService.getTableRows( matchesBySeason, seasonPlayers,seasonPlayerDetails);
        List<TableResultRow> result = getSortedTableRows( tableRows);
        return prepareTableResponse( result,null );
    }


}

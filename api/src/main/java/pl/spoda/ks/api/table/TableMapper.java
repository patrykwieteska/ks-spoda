package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.api.table.model.TableResult;
import pl.spoda.ks.api.table.model.TableResultRow;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.dto.LeagueTableRowDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.*;


@Service
@RequiredArgsConstructor
public class TableMapper {
    public TableResult mapToTableResult(List<LeagueTableRowDto> leagueTableRowDto,Set<PlayerData> playerList) {
        return TableResult.builder()
                .header( "TABELA WSZECHCZASÓW" )
                .tableRows( mapTableRows( leagueTableRowDto,playerList))
                .build();
    }

    private List<TableResultRow> mapTableRows(List<LeagueTableRowDto> leagueTableRowDto, Set<PlayerData> playerList) {
        return leagueTableRowDto.stream()
                .map( tableRow -> mapTableRow( playerList, tableRow ) )
                .sorted( Comparator.comparing( TableResultRow::getRating )
                        .thenComparing( tableRow -> tableRow.getPlayer().getAlias(), Collator.getInstance(new Locale(
                                "pl") )))
                .toList();
    }

    private TableResultRow mapTableRow(Set<PlayerData> playerList, LeagueTableRowDto tableRow) {
        PlayerData playerData = getPlayer( playerList, tableRow.getPlayerId() )
                .orElseThrow( () -> new SpodaApplicationException( "Cannot map player to league table" ) );

        return TableResultRow.builder()
                .player( playerData )
                .matches( tableRow.getMatches() )
                .rating( tableRow.getRating() )
                .pointsTotal( tableRow.getPointsTotal() )
                .pointsPerMatch( calculatePointsPerMatch( tableRow.getMatches(), tableRow.getPointsTotal() ) )
                .wins( tableRow.getWins() )
                .draws( tableRow.getDraws() )
                .loses( tableRow.getLoses() )
                .goalsScored( tableRow.getGoalScored() )
                .goalsConceded( tableRow.getGoalsConceded() )
                .goalsDiff( tableRow.getGoalScored() - tableRow.getGoalsConceded() )
                .playerForm( mapPlayerForm(tableRow.getPlayerForm()) )
                .build();

    }

    private List<String> mapPlayerForm(String playerForm) {
        if(playerForm == null) {
            return Collections.emptyList();
        }
        return List.of(  playerForm.split( "," ) );
    }

    private BigDecimal calculatePointsPerMatch(Integer matches, Integer pointsTotal) {
        return matches.equals( 0 )
                ? BigDecimal.ZERO
                : BigDecimal.valueOf( pointsTotal ).divide( BigDecimal.valueOf( matches ), RoundingMode.HALF_UP );
    }

    private Optional<PlayerData> getPlayer(Set<PlayerData> playerList, Integer playerId) {
        return playerList.stream()
                .filter( player -> player.getId().equals( playerId ) )
                .findFirst();

    }
}

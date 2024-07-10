package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.player.PlayerMapper;
import pl.spoda.ks.api.table.model.TableResultRow;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.database.dto.TableBaseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableRowsService {

    /*
     * Podczas zapisywania tabel nie zapisujemy pozycji gracza w tabeli.
     * Podczas odczytywania tabel sortujemy i generujemy pozycje w tabeli.
     */

    private final PlayerMapper playerMapper;

    public <T extends TableBaseDto> Set<TableResultRow> getTableRows(
            List<MatchDto> matchesInTable,
            Set<PlayerDto> playersInTable,
            Set<T> table) {
        return playersInTable.stream()
                .map( player -> {
                    List<MatchDto> playerMatches = getPlayerMatches( matchesInTable, player );
                    T tablePlayerDetails = getPlayerRating( table, player );
                    return mapToTableRow( player, playerMatches, tablePlayerDetails );

                } )
                .collect( Collectors.toSet() );
    }

    private List<MatchDto> getPlayerMatches(List<MatchDto> matchesInTable, PlayerDto player) {
        return matchesInTable.stream()
                .filter( match -> isPlayerInMatch( player, match ) )
                .toList();
    }

    private <T extends TableBaseDto> T getPlayerRating(
            Set<T> tableDetails,
            PlayerDto player
    ) {
        return tableDetails.stream()
                .filter( details -> player.equals( details.getPlayer() ) )
                .findFirst()
                .orElseThrow( () -> new SpodaApplicationException( " not found for player: " + player.getId() ) );
    }

    private boolean isPlayerInMatch(PlayerDto player, MatchDto match) {
        return isPlayerInHomeTeam( match, player ) || isPlayerInAwayTeam( match, player );
    }

    private <T extends TableBaseDto> TableResultRow mapToTableRow(
            PlayerDto player,
            List<MatchDto> playerMatches,
            T tablePlayerDetails
    ) {
        int wins = 0;
        int draws = 0;
        int loses = 0;
        int goalsScored = 0;
        int goalsConceded = 0;
        for (MatchDto match : playerMatches
        ) {
            if (isPlayerInHomeTeam( match, player )) {
                wins = wins + calculateWins( match.getHomeGoals(), match.getAwayGoals() );
                draws = draws + calculateDraws( match.getHomeGoals(), match.getAwayGoals() );
                loses = loses + calculateLoses( match.getHomeGoals(), match.getAwayGoals() );
                goalsScored = goalsScored + match.getHomeGoals();
                goalsConceded = goalsConceded + match.getAwayGoals();
            } else {
                wins = wins + calculateWins( match.getAwayGoals(), match.getHomeGoals() );
                draws = draws + calculateDraws( match.getAwayGoals(), match.getHomeGoals() );
                loses = loses + calculateLoses( match.getAwayGoals(), match.getHomeGoals() );
                goalsScored = goalsScored + match.getAwayGoals();
                goalsConceded = goalsConceded + match.getHomeGoals();
            }
        }

        BigDecimal pointsTotal =
                BigDecimal.valueOf( 3 ).multiply( BigDecimal.valueOf( wins ) ).add( BigDecimal.valueOf( draws ) );
        int matchListSize = playerMatches.size();
        return TableResultRow.builder()
                .player( playerMapper.mapToPlayerData( player ) )
                .pointsTotal( pointsTotal )
                .wins( wins )
                .loses( loses )
                .draws( draws )
                .goalsScored( goalsScored )
                .goalsConceded( goalsConceded )
                .goalsDiff( goalsScored - goalsConceded )
                .matches( matchListSize )
                .pointsPerMatch( getPointPerMatch( pointsTotal, matchListSize ) )
                .playerForm( calculatePlayerForm( player, playerMatches ) )
                .rating( Optional.ofNullable( tablePlayerDetails.getMatchCurrentRating() ).orElse( tablePlayerDetails.getCurrentRating() ) )
                .previousRating( tablePlayerDetails.getPreviousRating() )
                .matchInProgress( tablePlayerDetails.getMatchInProgress() )
                .build();
    }

    private BigDecimal getPointPerMatch(BigDecimal pointsTotal, int matchCount) {
        if (matchCount == 0) {
            return BigDecimal.ZERO;
        }

        return pointsTotal.divide( BigDecimal.valueOf( matchCount ), 2, RoundingMode.HALF_UP );
    }

    private List<String> calculatePlayerForm(PlayerDto player, List<MatchDto> playerMatches) {
        return playerMatches.stream().
                sorted( Comparator.comparing( MatchDto::getMatchTime ).reversed() )
                .limit( 5 )
                .map( match -> mapMatchToForm( match, player ) )
                .toList();
    }

    private String mapMatchToForm(MatchDto match, PlayerDto player) {
        if (isPlayerInHomeTeam( match, player )) {
            return calculatePlayerMatchResult( match.getHomeGoals(), match.getAwayGoals() );
        } else {
            return calculatePlayerMatchResult( match.getAwayGoals(), match.getHomeGoals() );
        }
    }

    private String calculatePlayerMatchResult(Integer playerGoals, Integer opponentGoals) {
        if (playerGoals > opponentGoals) {
            return "Z";
        }

        return playerGoals < opponentGoals
                ? "P"
                : "R";
    }

    private Integer calculateDraws(Integer playerGoals, Integer opponentGoals) {
        return Objects.equals( playerGoals, opponentGoals )
                ? 1
                : 0;
    }

    private Integer calculateLoses(Integer playerGoals, Integer opponentGoals) {
        return playerGoals < opponentGoals
                ? 1
                : 0;
    }

    private Integer calculateWins(Integer playerGoals, Integer opponentGoals) {
        return playerGoals > opponentGoals
                ? 1
                : 0;
    }


    private boolean isPlayerInHomeTeam(MatchDto match, PlayerDto player) {
        return match.getHomeTeam().getTeamPlayers().contains( player );
    }

    private boolean isPlayerInAwayTeam(MatchDto match, PlayerDto player) {
        return match.getAwayTeam().getTeamPlayers().contains( player );
    }
}

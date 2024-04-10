package pl.spoda.ks.api.matchday.players;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.model.MatchDayPlayerRow;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.PlayerDto;

import java.util.Comparator;
import java.util.List;

@Service
public class OpponentsService {

    public List<MatchDayPlayerRow> getPlayerOpponents(String playerAlias, List<MatchDto> matchesByMatchDay, List<String> matchDayPlayers) {
        List<MatchDayPlayerRow> matchDayPlayerRowList = matchDayPlayers.stream()
                .filter( player -> !player.equals( playerAlias ) )
                .map( player -> MatchDayPlayerRow.builder()
                        .alias( player )
                        .value( getOpponents( playerAlias, matchesByMatchDay, player ) )
                        .build() )
                .sorted( Comparator.comparing( MatchDayPlayerRow::getValue ) )
                .toList();

        int maxValue = matchDayPlayerRowList.stream().map( MatchDayPlayerRow::getValue ).max( Integer::compareTo ).orElse( -1 );
        int minValue = matchDayPlayerRowList.stream().map( MatchDayPlayerRow::getValue ).min( Integer::compareTo ).orElse( -1 );

        for (MatchDayPlayerRow matchDayPlayerRow : matchDayPlayerRowList) {
            if (matchDayPlayerRow.getValue() == minValue) {
                matchDayPlayerRow.setIsLowest( true );
                continue;
            }

            if (matchDayPlayerRow.getValue() == maxValue) {
                matchDayPlayerRow.setIsHighest( true );
            }
        }

        return matchDayPlayerRowList;
    }

    private Integer getOpponents(String playerAlias, List<MatchDto> matchesByMatchDay, String opponent) {
        int opponentMatchCount = 0;

        for (MatchDto match : matchesByMatchDay) {

            List<String> homePlayers = match.getHomeTeam().getTeamPlayers().stream().map( PlayerDto::getAlias ).toList();
            List<String> awayPlayers = match.getAwayTeam().getTeamPlayers().stream().map( PlayerDto::getAlias ).toList();

            if (!playerAndOpponentInMatch( homePlayers, awayPlayers, playerAlias, opponent )) {
                continue;
            }

            if (homePlayers.contains( opponent ) && awayPlayers.contains( playerAlias )
                    || homePlayers.contains( playerAlias ) && awayPlayers.contains( opponent )
            ) {
                opponentMatchCount++;
            }
        }
        return opponentMatchCount;

    }

    private boolean playerAndOpponentInMatch(List<String> homePlayers, List<String> awayPlayers, String playerAlias, String opponent) {
        return (homePlayers.contains( opponent ) || awayPlayers.contains( opponent )) && (homePlayers.contains( playerAlias ) || awayPlayers.contains( playerAlias ));
    }


}


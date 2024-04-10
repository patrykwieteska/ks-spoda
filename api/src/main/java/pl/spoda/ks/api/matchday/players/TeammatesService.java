package pl.spoda.ks.api.matchday.players;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.model.MatchDayPlayerRow;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.PlayerDto;

import java.util.Comparator;
import java.util.List;

@Service
public class TeammatesService {

    public List<MatchDayPlayerRow> getPlayerTeammates(String playerAlias, List<MatchDto> matchesByMatchDay, List<String> matchDayPlayers) {
        List<MatchDayPlayerRow> matchDayPlayerRowList = matchDayPlayers.stream()
                .filter( player -> !player.equals( playerAlias ) )
                .map( player -> MatchDayPlayerRow.builder()
                        .alias( player )
                        .value( getTeammates( playerAlias, matchesByMatchDay, player ) )
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

    private Integer getTeammates(String playerAlias, List<MatchDto> matchesByMatchDay, String teammate) {
        int teammatesCount = 0;

        for (MatchDto match : matchesByMatchDay) {

            List<String> homePlayers = match.getHomeTeam().getTeamPlayers().stream().map( PlayerDto::getAlias ).toList();
            List<String> awayPlayers = match.getAwayTeam().getTeamPlayers().stream().map( PlayerDto::getAlias ).toList();

            if (playerAndTeammateInMatch( homePlayers, awayPlayers, playerAlias, teammate )) {
                teammatesCount++;
            }
        }
        return teammatesCount;

    }

    private boolean playerAndTeammateInMatch(List<String> homePlayers, List<String> awayPlayers, String playerAlias, String teammate) {
        return (homePlayers.contains( teammate ) && homePlayers.contains( playerAlias )) || (awayPlayers.contains( teammate ) && awayPlayers.contains( playerAlias ));
    }
}

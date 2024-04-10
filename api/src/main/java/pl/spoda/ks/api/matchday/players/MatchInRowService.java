package pl.spoda.ks.api.matchday.players;

import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.PlayerDto;

import java.util.Comparator;
import java.util.List;

@Service
public class MatchInRowService {

    public Integer getMatchesInRow(String playerAlias, List<MatchDto> matchesByMatchDay) {
        List<MatchDto> sortedMatches =
                matchesByMatchDay.stream().sorted( Comparator.comparing( MatchDto::getMatchTime ).reversed()).toList();
        int matchesInRow = 0;
        for (MatchDto matchDto : sortedMatches) {

            if (isPlayerInTheMatch( playerAlias, matchDto )) {
                matchesInRow++;
            } else {
                break;
            }
        }
        return matchesInRow;
    }

    private boolean isPlayerInTheMatch(String playerAlias, MatchDto matchDto) {
        List<String> awayPlayers = matchDto.getAwayTeam().getTeamPlayers().stream().map( PlayerDto::getAlias ).toList();
        List<String> homePlayers = matchDto.getHomeTeam().getTeamPlayers().stream().map( PlayerDto::getAlias ).toList();

        return awayPlayers.contains( playerAlias ) || homePlayers.contains( playerAlias );

    }
}

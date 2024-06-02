package pl.spoda.ks.api.match;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.euro.EuroMatchSchedule;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.euro.EuroService;
import pl.spoda.ks.euro.model.EuroMatch;
import pl.spoda.ks.euro.model.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EuroMatchService {

    private final EuroService euroService;

    public void updateEuroMatch(
            MatchDto matchDto,
            Boolean isEuro,
            boolean isMatchComplete
    ) {
        if (BooleanUtils.isNotTrue( isEuro )) {
            return;
        }

        Integer euroMatchId = matchDto.getEuroMatchId();
        if (euroMatchId == null) {
            throw new SpodaApplicationException( "Euro match number cannot be null" );
        }


        List<Player> homePlayers = getPlayerList( matchDto.getHomeTeam().getTeamPlayers() );
        List<Player> awayPlayers = getPlayerList( matchDto.getAwayTeam().getTeamPlayers() );
        euroService.addResult(
                homePlayers,
                awayPlayers,
                matchDto.getHomeGoals(),
                matchDto.getAwayGoals(),
                euroMatchId,
                isMatchComplete,
                matchDto.getAwayPenaltyGoals(),
                matchDto.getHomePenaltyGoals()
        );
    }

    private List<Player> getPlayerList(Set<PlayerDto> teamPlayers) {
        return teamPlayers.stream()
                .map( this::mapToEuroMatchPlayer )
                .toList();
    }

    private Player mapToEuroMatchPlayer(PlayerDto player) {
        return Player.builder()
                .alias( player.getAlias() )
                .imageUrl( player.getPlayerImg() )
                .externalPlayerId( player.getId() )
                .build();
    }

    public void resetEuroMatch(Integer euroMatchId) {
        if(euroMatchId == null) {
            return;
        }

        euroService.clearEuroMatch( euroMatchId );
    }

    public EuroMatchSchedule getNotPlayedMatches() {
        List<EuroMatch> nextMatches = euroService.getEuroCalendar( null )
                .getEuroMatches().stream()
                .filter( euroMatch -> !euroMatch.isPlayed() )
                .sorted( Comparator.comparing( EuroMatch::getMatchNumber ) )
                .toList();

        return EuroMatchSchedule.builder()
                .matches( nextMatches )
                .build();
    }

    public EuroMatch getNextEuroMatch() {
        return euroService.getEuroCalendar( null )
                .getEuroMatches().stream()
                .filter( euroMatch -> !euroMatch.isPlayed() )
                .sorted( Comparator.comparing( EuroMatch::getMatchNumber ) )
                .limit( 1 )
                .findFirst()
                .orElseThrow(() -> new SpodaApplicationException("Brak meczów, prawdopodobnie turniej został " +
                        "zakończony"));
    }

    public EuroMatchSchedule getPlayedMatches(String group, long limit) {
        if(limit <=0 || limit > 51) {
            limit=51;
        }

        List<EuroMatch> nextMatches = euroService.getEuroCalendar( group )
                .getEuroMatches().stream()
                .filter( EuroMatch::isPlayed )
                .sorted( Comparator.comparing( EuroMatch::getMatchNumber ) )
                .limit( limit )
                .toList();

        return EuroMatchSchedule.builder()
                .matches( nextMatches )
                .build();
    }
}

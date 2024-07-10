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
            boolean isMatchComplete,
            String euroId
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
                matchDto.getHomePenaltyGoals(),
                euroId
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

    public void resetEuroMatch(Integer euroMatchId, String euroId) {
        if(euroMatchId == null) {
            return;
        }

        euroService.clearEuroMatch( euroMatchId,euroId );
    }

    public EuroMatchSchedule getNotPlayedMatches(String euroId) {
        List<EuroMatch> nextMatches = euroService.getEuroCalendar( null,euroId )
                .getEuroMatches().stream()
                .filter( euroMatch -> !euroMatch.isPlayed() )
                .sorted( Comparator.comparing( EuroMatch::getMatchNumber ) )
                .toList();

        return EuroMatchSchedule.builder()
                .matches( nextMatches )
                .build();
    }

    public EuroMatch getNextEuroMatch(String euroId) {
        return euroService.getEuroCalendar( null,euroId )
                .getEuroMatches().stream()
                .filter( euroMatch -> !euroMatch.isPlayed() )
                .sorted( Comparator.comparing( EuroMatch::getDateTime )
                        .thenComparing( EuroMatch::getMatchNumber ) )
                .limit( 1 )
                .findFirst()
                .orElse( EuroMatch.builder().message("Brak następnych meczów. Turniej zakończony").build());
    }

    public EuroMatchSchedule getPlayedMatches(String group, long limit, String euroId) {
        if(limit <=0 || limit > 51) {
            limit=51;
        }

        List<EuroMatch> nextMatches = euroService.getEuroCalendar( group,euroId )
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

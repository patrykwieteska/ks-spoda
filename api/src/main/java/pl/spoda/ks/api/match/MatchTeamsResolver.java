package pl.spoda.ks.api.match;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.spoda.ks.euro.EuroService;

@Service
@RequiredArgsConstructor
public class MatchTeamsResolver {

    private final EuroService euroService;

    public Pair<Integer, Integer> prepareMatchTeams(
            Integer homeGameTeamId,
            Integer awayGameTeamId,
            Integer euroMatchId,
            String euroId
    ) {
        return euroMatchId == null
                ? Pair.of( homeGameTeamId,awayGameTeamId )
                : euroService.getMatchTeams( euroMatchId,euroId );

    }
}

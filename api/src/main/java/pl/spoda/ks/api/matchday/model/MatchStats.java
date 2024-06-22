package pl.spoda.ks.api.matchday.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchStats {

    private long games;
    private MatchStatsResult greatestVictory;
    private MatchStatsResult biggestFailure;
    private MatchStatsResult lastMatch;
    private MatchPlayers commonOpponents;
    private MatchPlayers commonTeammates;

}

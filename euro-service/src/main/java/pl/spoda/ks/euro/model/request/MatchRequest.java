package pl.spoda.ks.euro.model.request;

import lombok.Builder;
import lombok.Data;
import pl.spoda.ks.euro.model.Player;
import pl.spoda.ks.euro.model.TournamentStage;

import java.util.List;

@Data
@Builder
public class MatchRequest {

    private List<Player> homePlayers;
    private List<Player> awayPlayers;
    private int homeGoals;
    private int awayGoals;
    private int matchNumber;
    private TournamentStage stage;
    private String matchDayId;
    private boolean isFinished;

}

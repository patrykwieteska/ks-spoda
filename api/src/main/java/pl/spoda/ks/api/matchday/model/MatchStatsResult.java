package pl.spoda.ks.api.matchday.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import pl.spoda.ks.api.match.model.GameTeam;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MatchStatsResult {

    private Integer teamGoals;
    private Integer opponentGoals;
    private List<String> opponents;
    private String teammate;
    private GameTeam team;
    private GameTeam opponentTeam;
    private LocalDateTime matchDate;
    @JsonIgnore
    private Integer goalsDiff;

}

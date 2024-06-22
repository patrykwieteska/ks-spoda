package pl.spoda.ks.euro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupStageTeam {

    private String id;
    private Team team;
    private TournamentGroup groupCode;
    private int points;
    private int wins;
    private int draws;
    private int loses;
    private int goalsScored;
    private int goalsConceded;
    private int groupPosition;
    private boolean matchInProgress;
}

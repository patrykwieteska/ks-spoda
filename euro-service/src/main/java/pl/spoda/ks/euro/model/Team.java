package pl.spoda.ks.euro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Team {

    private Integer teamId;
    private int ranking;
    private String name;
    private String engName;
    private String flag;
    private TournamentGroup groupCode;

    public Team(int teamId, int ranking, String name, String engName,String flag, TournamentGroup groupCode) {
        this.teamId = teamId;
        this.ranking = ranking;
        this.name = name;
        this.engName = engName;
        this.flag = flag;
        this.groupCode = groupCode;
    }


}


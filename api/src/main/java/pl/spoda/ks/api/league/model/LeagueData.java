package pl.spoda.ks.api.league.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.api.league.enums.TeamStructure;

@Data
@ToString
@Builder
public class LeagueData {

    private Integer leagueId;
    private String name;
    private String description;
    private TeamStructure teamStructure;
    private LeagueType type;
}

package pl.spoda.ks.api.league.model;

import lombok.*;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.enums.LeagueType;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeagueRequest {

    private String name;
    private String description;
    private TeamStructure teamStructure;
    private LeagueType type;

}

package pl.spoda.ks.api.league.model;

import lombok.*;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.api.player.model.PlayerData;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeagueRequest {

    private String name;
    private String logoUrl;
    private String description;
    private TeamStructure teamStructure;
    private LeagueType type;
    private List<PlayerData> playerList;

}

package pl.spoda.ks.api.league.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    @Size(min = 4, max = 25, message = "League name should has length between 4 and 25")
    private String name;
    private String logoUrl;
    private String description;
    @NotNull(message = "Team structure cannot be null")
    private TeamStructure teamStructure;
    @NotNull(message = "League type cannot be null")
    private LeagueType type;
    @Size(min=2)
    private List<PlayerData> playerList;
    private Boolean isPrivate;

}

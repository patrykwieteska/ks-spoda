package pl.spoda.ks.api.match.model;

import lombok.*;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.database.dto.GameTeamDto;

import java.util.Set;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchTeam {

    private Set<PlayerData> matchPlayers;
    private GameTeamDto gameTeam;
}

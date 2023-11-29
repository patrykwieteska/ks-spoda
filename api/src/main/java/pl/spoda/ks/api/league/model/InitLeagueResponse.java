package pl.spoda.ks.api.league.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.player.model.PlayerData;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitLeagueResponse extends BaseResponse {

    private LeagueData league;
    private Set<PlayerData> playerList;
}
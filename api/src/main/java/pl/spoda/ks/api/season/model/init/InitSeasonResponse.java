package pl.spoda.ks.api.season.model.init;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.league.model.LeagueData;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.api.season.model.SeasonData;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitSeasonResponse extends BaseResponse {

    private Integer seasonId;
    private LeagueData leagueData;
    private SeasonData seasonData;
    private Set<PlayerData> leaguePlayers;
}

package pl.spoda.ks.api.league.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.api.season.model.SeasonData;
import pl.spoda.ks.api.table.model.TableResult;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitLeagueResponse extends BaseResponse {

    private LeagueData league;
    private List<SeasonData> seasons;
    private Set<PlayerData> playerList;
    private TableResult leagueTable;
}
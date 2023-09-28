package pl.spoda.ks.api.season.model.init;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.enums.LeagueType;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitSeasonResponse extends BaseResponse {

    private String name;
    private String description;
    private SeasonData seasonData;
    private Integer leagueId;
    private TeamStructure teamStructure;
    private LeagueType type;
}

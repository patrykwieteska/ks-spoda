package pl.spoda.ks.api.season.model.init;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitSeasonResponse extends BaseResponse {

    private String leagueName;
    private String leagueDescription;
    private SeasonData seasonData;
    private Integer leagueId;
}

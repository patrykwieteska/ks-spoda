package pl.spoda.ks.api.matchday.model.init;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitMatchDayResponse extends BaseResponse {

    private String leagueName;
    private String leagueDescription;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;
    private MatchDayData matchDay;
}

package pl.spoda.ks.api.matchday.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.database.dto.MatchDayDto;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitMatchDayResponse extends BaseResponse {
    private MatchDayDto matchDay;
}

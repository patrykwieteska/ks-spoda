package pl.spoda.ks.api.matchday.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDayStored extends BaseResponse {

    private Integer matchDayId;

}

package pl.spoda.ks.api.league.model.response;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.model.response.BaseResponse;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LeagueCreatedResponse extends BaseResponse {
    private Integer leagueId;
}

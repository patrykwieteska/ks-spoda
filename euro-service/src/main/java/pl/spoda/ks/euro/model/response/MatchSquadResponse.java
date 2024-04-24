package pl.spoda.ks.euro.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MatchSquadResponse {

    Integer awayTeamId;
    Integer homeTeamId;
}

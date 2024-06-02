package pl.spoda.ks.api.match.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PenaltyKicks {

    private int homeGoals;
    private int awayGoals;
}

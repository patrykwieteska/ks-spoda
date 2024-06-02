package pl.spoda.ks.euro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PenaltyKicks {

    Integer homeGoals;
    Integer awayGoals;

}

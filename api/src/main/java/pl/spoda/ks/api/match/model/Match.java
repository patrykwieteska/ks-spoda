package pl.spoda.ks.api.match.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class Match extends BaseResponse {

    private Integer id;
    private LocalDateTime matchTime;
    private Boolean isFinished;
    private MatchTeam homeTeam;
    private MatchTeam awayTeam;
    private Integer homeGoals;
    private Integer awayGoals;


}

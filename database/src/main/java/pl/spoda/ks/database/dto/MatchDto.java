package pl.spoda.ks.database.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class MatchDto extends BaseDto {

    private Integer id;
    private Integer matchDayId;
    private LocalDateTime matchTime;
    private Boolean isFinished;
    private Integer homeGoals;
    private Integer awayGoals;
    private MatchTeamDto homeTeam;
    private MatchTeamDto awayTeam;
    private Integer euroMatchId;
}

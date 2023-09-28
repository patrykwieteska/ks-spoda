package pl.spoda.ks.database.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MatchDto {

    private Integer id;
    private Integer matchDayId;
    private LocalDateTime matchTime;
    private Boolean isFinished;
    private String type;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private Integer homeGoals;
    private Integer awayGoals;
}

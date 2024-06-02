package pl.spoda.ks.api.match.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditMatchRequest {

    private Integer homeGoals;
    private Integer awayGoals;
    private Integer homeGameTeamId;
    private Integer awayGameTeamId;
    private Boolean isComplete;
    private Integer euroMatchId;
    private PenaltyKicks penalties;
}

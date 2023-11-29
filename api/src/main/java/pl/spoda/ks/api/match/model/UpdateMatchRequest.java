package pl.spoda.ks.api.match.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMatchRequest {

    @NotNull(message = "request.matchId is null")
    private Integer matchId;

    @NotNull(message = "request.homeGoals is null")
    private Integer homeGoals;

    @NotNull(message = "request.awayGoals is null")
    private Integer awayGoals;

    private Integer homeGameTeamId;

    private Integer awayGameTeamId;
}

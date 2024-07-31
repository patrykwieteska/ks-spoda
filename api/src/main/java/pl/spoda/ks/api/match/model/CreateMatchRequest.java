package pl.spoda.ks.api.match.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMatchRequest {

    @NotNull(message = "request.matchDayId is null")
    private Integer matchDayId;

    @NotEmpty(message = "Brak graczy gospodarzy")
    private List<Integer> homePlayers;

    @NotEmpty(message = "Brak graczy gości")
    private List<Integer> awayPlayers;

    @NotNull(message = "request.homeGoals is null")
    private Integer homeGoals;

    @NotNull(message = "request.awayGoals is null")
    private Integer awayGoals;

    private Integer homeGameTeamId;

    private Integer awayGameTeamId;
    private Integer euroMatchId;
    private Boolean isPlayOffMatch;
    private LocalDateTime matchTime;
}

package pl.spoda.ks.api.match.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @NotEmpty(message = "request.homePlayers is empty")
    private List<Integer> homePlayers;

    @NotEmpty(message = "request.awayPlayers is empty")
    private List<Integer> awayPlayers;

    @NotNull(message = "request.homeGoals is null")
    private Integer homeGoals;

    @NotNull(message = "request.awayGoals is null")
    private Integer awayGoals;

    private Integer homeGameTeamId;

    private Integer awayGameTeamId;
}

package pl.spoda.ks.rating.model.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;

@Data
@Builder
@Validated
@ValidateOnExecution
public class RatingRequest {

    @NotNull(message = "Team A cannot be null")
    private GameTeamData teamA;

    @NotNull(message = "Team A cannot be null")
    private GameTeamData teamB;

}

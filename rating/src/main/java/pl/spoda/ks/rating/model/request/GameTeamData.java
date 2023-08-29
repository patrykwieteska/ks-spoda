package pl.spoda.ks.rating.model.request;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;
import java.util.List;

@Data
@Builder
public class GameTeamData {

    @NotNull(message = "Goals cannot be null")
    @Min(value = 0, message = "Goals cannot be less than 0")
    @Max(value = 99, message = "Team has scored too many goals ;)")
    private Integer goals;

    @NotEmpty(message = "Players list cannot be empty")
    @Max(value = 2)
    private List<GamePlayerData> players;
}

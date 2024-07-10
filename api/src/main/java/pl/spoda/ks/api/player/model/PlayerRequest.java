package pl.spoda.ks.api.player.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PlayerRequest {

    private Integer playerId;
    @Size(min = 3, max=30, message = "Name size is between 3 and 20 characters")
    private String name;
    @Size(min = 3, max=30, message = "Alias size is between 3 and 20 characters")
    private String alias;
    @NotNull
    private Integer leagueId;
    private String playerImg;
}

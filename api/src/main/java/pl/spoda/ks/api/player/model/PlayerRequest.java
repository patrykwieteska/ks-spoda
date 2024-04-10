package pl.spoda.ks.api.player.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class PlayerRequest {

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, max=30, message = "Name size is between 3 and 20 characters")
    private String name;
    @NotNull(message = "Alias cannot be null")
    @Size(min = 3, max=30, message = "Alias size is between 3 and 20 characters")
    private String alias;
    private Integer leagueId;
    private String playerImg;
}

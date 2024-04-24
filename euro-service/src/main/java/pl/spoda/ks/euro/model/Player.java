package pl.spoda.ks.euro.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {

    @NotNull
    private String alias;
    private String imageUrl;
    @NotNull
    private Integer externalPlayerId;
}

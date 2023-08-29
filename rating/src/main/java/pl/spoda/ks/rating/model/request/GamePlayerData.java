package pl.spoda.ks.rating.model.request;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class GamePlayerData {

    @NotNull(message = "Player id cannot be null")
    private String id;

    @NotNull(message = "Rating cannot be null")
    private BigDecimal rating;

    private BigDecimal difference;
}

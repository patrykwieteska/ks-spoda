package pl.spoda.ks.database.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.comons.table.Competition;

import java.math.BigDecimal;

@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public abstract class TableBaseDto extends BaseDto implements Competition{

    private PlayerDto player;
    private BigDecimal currentRating;
    private Integer currentPosition;
    private Integer previousPosition;
    private Integer standbyPosition;
    private BigDecimal matches;
    private BigDecimal pointsTotal;
    private Boolean matchInProgress;
    private Boolean isNewPlayer;
}


package pl.spoda.ks.api.table.model;

import lombok.*;
import pl.spoda.ks.api.player.model.PlayerData;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableResultRow {

    private PlayerData player;
    private Integer matches;
    private BigDecimal rating;
    private BigDecimal previousRating;
    private BigDecimal pointsTotal;
    private BigDecimal pointsPerMatch;
    private Integer wins;
    private Integer draws;
    private Integer loses;
    private Integer goalsScored;
    private Integer goalsConceded;
    private Integer goalsDiff;
    private List<String> playerForm;
    private Integer currentPosition;
    private Integer previousPosition;
    private String previousPositionReference;
    private boolean matchInProgress;

}

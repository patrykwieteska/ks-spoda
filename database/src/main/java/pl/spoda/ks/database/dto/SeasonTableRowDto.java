package pl.spoda.ks.database.dto;

import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SeasonTableRowDto {

    private Integer id;
    private Integer playerId;
    private Integer seasonId;
    private Integer matches;
    private BigDecimal rating;
    private Integer pointsTotal;
    private Integer wins;
    private Integer draws;
    private Integer loses;
    private Integer goalScored;
    private Integer goalsConceded;
    private String playerForm;

}

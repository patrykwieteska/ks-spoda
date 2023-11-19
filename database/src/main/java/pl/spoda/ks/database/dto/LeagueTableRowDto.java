package pl.spoda.ks.database.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LeagueTableRowDto {

    private Integer id;
    private Integer playerId;
    private Integer leagueId;
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

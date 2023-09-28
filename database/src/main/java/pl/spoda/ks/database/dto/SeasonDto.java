package pl.spoda.ks.database.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SeasonDto {

    private Integer id;
    private Integer leagueId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isFinished;
    private BigDecimal initialRating;

}

package pl.spoda.ks.database.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class SeasonDto extends BaseDto {

    private Integer id;
    private Integer leagueId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isFinished;
    private BigDecimal initialRating;
    private String pointCountingMethod;
    private String ratingType;
    private Integer leagueSeasonCount;

}

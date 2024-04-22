package pl.spoda.ks.api.season.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.api.season.enums.RatingType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SeasonData extends BaseResponse {

    private Integer id;
    private Integer leagueId;
    private Integer seasonCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isFinished;
    private BigDecimal initialRating;
    private PointCountingMethod pointCountingMethod;
    private RatingType ratingType;
    private Boolean hasNoActiveMatchDay;
    private Boolean isEuro;
    private String image;
    private String seasonName;

}

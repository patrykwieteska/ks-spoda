package pl.spoda.ks.api.season.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.api.season.enums.RatingType;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeasonRequest {

    @NotNull(message = "request.startDate is null")
    private LocalDate startDate;
    @NotNull(message = "request.leagueId is null")
    private Integer leagueId;

    private RatingType ratingType;
    @NotNull(message = "request.pointCountingMethod is null")
    private PointCountingMethod pointCountingMethod;
    private Boolean isEuro;
    private String image;
    private String seasonName;

}

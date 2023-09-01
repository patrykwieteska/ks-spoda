package pl.spoda.ks.api.season.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeasonRequest {

    private LocalDate startDate;
    private Integer leagueId;

}

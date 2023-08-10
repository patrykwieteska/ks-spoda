package pl.spoda.ks.api.league.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeagueRequest {

    private String name;
    private String description;
    private LocalDate startDate;

}

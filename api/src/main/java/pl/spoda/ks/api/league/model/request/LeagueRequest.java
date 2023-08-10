package pl.spoda.ks.api.league.model.request;

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

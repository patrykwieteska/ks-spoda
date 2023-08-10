package pl.spoda.ks.api.round.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoundRequest {

    private Integer leagueId;
    private LocalDate roundDate;

}

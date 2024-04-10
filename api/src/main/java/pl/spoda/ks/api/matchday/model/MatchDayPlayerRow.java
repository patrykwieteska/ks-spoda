package pl.spoda.ks.api.matchday.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class MatchDayPlayerRow {

    private String alias;
    private int value;
    private Boolean isHighest;
    private Boolean isLowest;
}

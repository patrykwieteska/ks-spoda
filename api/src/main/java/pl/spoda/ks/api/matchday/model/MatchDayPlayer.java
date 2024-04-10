package pl.spoda.ks.api.matchday.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDayPlayer {

    private String alias;
    private List<MatchDayPlayerRow> opponents;
    private List<MatchDayPlayerRow> teammates;
    private Integer matchesInRow;
}

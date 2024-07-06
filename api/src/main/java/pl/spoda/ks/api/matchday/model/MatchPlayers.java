package pl.spoda.ks.api.matchday.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MatchPlayers {

    private List<String> players;
    private Integer gamesCount;
}

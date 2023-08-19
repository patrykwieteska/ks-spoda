package pl.spoda.ks.rating.model.request;

import lombok.Data;

import java.util.List;

@Data
public class GameTeamData {

    private Integer goals;
    private List<GamePlayerData> players;
}

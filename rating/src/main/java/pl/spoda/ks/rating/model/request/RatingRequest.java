package pl.spoda.ks.rating.model.request;

import lombok.Data;

@Data
public class RatingRequest {

    private GameTeamData homeTeam;
    private GameTeamData awayTeam;

}

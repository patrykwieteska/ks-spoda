package pl.spoda.ks.euro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EuroParams {

    private String id;
    private TournamentStage currentStage;

}

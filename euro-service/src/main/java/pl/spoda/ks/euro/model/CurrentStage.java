package pl.spoda.ks.euro.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentStage {

    private TournamentStage stage;
    private Integer selectedTab;
    private String description;

}

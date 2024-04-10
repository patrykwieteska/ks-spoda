package pl.spoda.ks.api.match.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableGameTeams extends BaseResponse {

    private List<GameTeam> gameTeams;
}

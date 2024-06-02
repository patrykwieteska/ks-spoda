package pl.spoda.ks.euro.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.spoda.ks.euro.model.GroupStageTeam;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPlacesResponse {

    List<GroupStageTeam> teams;
}

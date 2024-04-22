package pl.spoda.ks.euro.model.response;

import lombok.Builder;
import lombok.Data;
import pl.spoda.ks.euro.model.GroupStageTeam;

import java.util.List;

@Data
@Builder
public class EuroGroup {

    private String groupCode;
    private List<GroupStageTeam> teams;
}

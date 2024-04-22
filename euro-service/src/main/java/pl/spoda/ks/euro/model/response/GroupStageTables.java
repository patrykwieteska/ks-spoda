package pl.spoda.ks.euro.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupStageTables {

    private List<EuroGroup> groupList;
}

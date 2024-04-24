package pl.spoda.ks.api.euro;

import lombok.Builder;
import lombok.Data;
import pl.spoda.ks.euro.model.EuroMatch;

import java.util.List;

@Data
@Builder
public class EuroMatchSchedule {
    private List<EuroMatch> matches;
    
}

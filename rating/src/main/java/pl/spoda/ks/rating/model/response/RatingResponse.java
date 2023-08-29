package pl.spoda.ks.rating.model.response;

import lombok.Builder;
import lombok.Data;
import pl.spoda.ks.rating.model.request.GamePlayerData;

import java.util.List;

@Data
@Builder
public class RatingResponse {
    private List<GamePlayerData> players;
}

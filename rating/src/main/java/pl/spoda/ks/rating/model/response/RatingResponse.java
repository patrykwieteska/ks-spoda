package pl.spoda.ks.rating.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RatingResponse {
    private Map<String, BigDecimal> playersRating;
}

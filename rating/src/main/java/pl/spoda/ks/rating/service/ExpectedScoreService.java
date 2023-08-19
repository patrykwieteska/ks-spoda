package pl.spoda.ks.rating.service;

import org.springframework.stereotype.Service;
import pl.spoda.ks.rating.model.request.RatingRequest;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExpectedScoreService {
    public Map<String, BigDecimal> calculateExpectedScores(RatingRequest request) {
        throw new IllegalArgumentException("Not implemented yet");
    }
}

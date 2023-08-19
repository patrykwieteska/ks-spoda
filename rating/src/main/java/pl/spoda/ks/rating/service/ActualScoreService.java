package pl.spoda.ks.rating.service;

import org.springframework.stereotype.Service;
import pl.spoda.ks.rating.model.request.RatingRequest;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ActualScoreService {


    public Map<String, BigDecimal> calculateActualScores(RatingRequest request, Map<String, BigDecimal> expectedScores) {
        throw new IllegalArgumentException("Not implemented yet");
    }
}

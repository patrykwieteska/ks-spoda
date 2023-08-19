package pl.spoda.ks.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.rating.model.request.RatingRequest;
import pl.spoda.ks.rating.model.response.RatingResponse;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final ExpectedScoreService expectedScoreService;
    private final ActualScoreService actualScoreService;

    public RatingResponse calculateRating(RatingRequest request) {
        Map<String, BigDecimal> expectedScores = expectedScoreService.calculateExpectedScores(request);
        Map<String, BigDecimal> actualScores = actualScoreService.calculateActualScores(request,expectedScores);


        //TODO
         throw new IllegalArgumentException("Not implemented yet");
    }
}

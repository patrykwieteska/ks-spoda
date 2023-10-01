package pl.spoda.ks.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.rating.model.request.RatingRequest;
import pl.spoda.ks.rating.model.response.RatingResponse;
import pl.spoda.ks.rating.service.RatingService;

@RestController
@RequestMapping("/api/test/rating")
@ConditionalOnProperty("rating.controller.enabled")
@RequiredArgsConstructor
public class RatingTestController {

    private final RatingService ratingService;


    @PostMapping()
    @LogEvent
    public RatingResponse calculateRating(
            @RequestBody RatingRequest request
    ) {
        return ratingService.calculateRating( request );
    }
}

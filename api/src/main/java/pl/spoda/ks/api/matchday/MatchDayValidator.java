package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;

@Service
@RequiredArgsConstructor
public class MatchDayValidator {

    public void validateMatchDay(CreateMatchDayRequest request) {
        if (request.getSeasonId() == null)
            throw new SpodaApplicationException("Parameter seasonId is required");

        if (request.getMatchDayDate() == null)
            throw new SpodaApplicationException("Parameter matchDayDate is required");
    }
}

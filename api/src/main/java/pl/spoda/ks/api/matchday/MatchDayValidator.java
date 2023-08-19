package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.repository.MatchDayServiceDb;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;

@Service
@RequiredArgsConstructor
public class MatchDayValidator {

    private final MatchDayServiceDb matchDayServiceDb;

    public void validateMatchDay(CreateMatchDayRequest request) {
        if (request.getLeagueId() == null)
            throw new SpodaApplicationException("Parameter leagueId is required");

        if (request.getMatchDayDate() == null)
            throw new SpodaApplicationException("Parameter matchDayDate is required");
    }
}

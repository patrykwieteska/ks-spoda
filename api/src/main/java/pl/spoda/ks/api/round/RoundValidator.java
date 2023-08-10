package pl.spoda.ks.api.round;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.repository.RoundServiceDb;
import pl.spoda.ks.api.round.model.CreateRoundRequest;

@Service
@RequiredArgsConstructor
public class RoundValidator {

    private final RoundServiceDb roundServiceDb;

    public void validateRound(CreateRoundRequest request) {
        if (request.getLeagueId() == null)
            throw new SpodaApplicationException("Parameter leagueId is required");

        if (request.getRoundDate() == null)
            throw new SpodaApplicationException("Parameter roundDate is required");
    }
}

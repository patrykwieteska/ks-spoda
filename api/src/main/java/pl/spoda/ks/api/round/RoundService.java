package pl.spoda.ks.api.round;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.round.model.CreateRoundRequest;
import pl.spoda.ks.api.round.model.RoundCreatedResponse;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.database.dto.RoundDto;
import pl.spoda.ks.database.repository.RoundServiceDb;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundServiceDb roundServiceDb;
    private final RoundValidator roundValidator;
    private final RoundMapper roundMapper;
    private final ResponseResolver responseResolver;

    @LogEvent
    public ResponseEntity<BaseResponse> createRound(CreateRoundRequest request) {
        roundValidator.validateRound( request );
        RoundDto round = roundServiceDb.createRound( roundMapper.mapRound(request) );
        return responseResolver.prepareResponseCreated( RoundCreatedResponse.builder().roundId( round.getId() ).build() );
    }
}

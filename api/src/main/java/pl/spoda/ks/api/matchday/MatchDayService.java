package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.api.matchday.model.InitMatchDayResponse;
import pl.spoda.ks.api.matchday.model.MatchDayStored;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.repository.MatchDayServiceDb;

@Service
@RequiredArgsConstructor
public class MatchDayService {

    private final MatchDayServiceDb matchDayServiceDb;
    private final pl.spoda.ks.api.matchday.MatchDayValidator matchDayValidator;
    private final MatchDayMapper matchDayMapper;
    private final ResponseResolver responseResolver;

    @LogEvent
    public ResponseEntity<BaseResponse> createMatchDay(CreateMatchDayRequest request) {
        matchDayValidator.validateMatchDay( request );
        MatchDayDto matchDay = matchDayServiceDb.createMatchDay( matchDayMapper.mapMatchDay(request) );
        return responseResolver.prepareResponseCreated( MatchDayStored.builder().matchDayId( matchDay.getId() ).build() );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> completeMatchDay(MatchDayStored request) {
        matchDayServiceDb.completeMatchDay(request.getMatchDayId());
        return responseResolver.prepareResponse( request );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> initMatchDay(Integer matchDayId) {
        MatchDayDto matchDayDto =  matchDayServiceDb.getMatchDay(matchDayId );
        InitMatchDayResponse response = InitMatchDayResponse.builder()
                .matchDay( matchDayDto )
                .build();
        return responseResolver.prepareResponse( response );
    }
}

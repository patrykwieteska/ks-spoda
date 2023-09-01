package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.DeleteResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.api.matchday.model.init.InitMatchDayResponse;
import pl.spoda.ks.api.matchday.model.MatchDayStored;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.repository.LeagueServiceDb;
import pl.spoda.ks.database.repository.MatchDayServiceDb;
import pl.spoda.ks.database.repository.SeasonServiceDb;

@Service
@RequiredArgsConstructor
public class MatchDayService {

    private final MatchDayServiceDb matchDayServiceDb;
    private final MatchDayValidator matchDayValidator;
    private final MatchDayMapper matchDayMapper;
    private final ResponseResolver responseResolver;
    private final InitMatchDayMapper initMatchDayMapper;
    private final SeasonServiceDb seasonServiceDb;
    private final LeagueServiceDb leagueServiceDb;

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
        SeasonDto seasonDto = seasonServiceDb.getSingleSeason( matchDayDto.getSeasonId() );
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague( seasonDto.getLeagueId() );

        InitMatchDayResponse response = initMatchDayMapper.mapToResponse(matchDayDto,seasonDto,leagueDto);
        return responseResolver.prepareResponse( response );
    }

    public ResponseEntity<BaseResponse> deleteMatchDay(Integer matchDayId) {
        matchDayServiceDb.deleteMatchDay(matchDayId);
        return responseResolver.prepareResponse( DeleteResponse.builder().deletedId( matchDayId ).build() );
    }
}

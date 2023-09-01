package pl.spoda.ks.api.season;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.season.model.init.InitSeasonResponse;
import pl.spoda.ks.api.season.model.SeasonRequest;
import pl.spoda.ks.api.season.model.StoredSeason;
import pl.spoda.ks.api.season.model.SeasonListResponse;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.repository.LeagueServiceDb;
import pl.spoda.ks.database.repository.MatchDayServiceDb;
import pl.spoda.ks.database.repository.SeasonServiceDb;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonMapper seasonMapper;
    private final SeasonServiceDb seasonServiceDb;
    private final ResponseResolver responseResolver;
    private final MatchDayServiceDb matchDayServiceDb;
    private final LeagueServiceDb leagueServiceDb;
    private final InitSeasonMapper initSeasonMapper;

    @LogEvent
    public ResponseEntity<BaseResponse> createLeague(SeasonRequest request) {
        SeasonDto result = seasonServiceDb.save( seasonMapper.mapSeason( request ) );
        return responseResolver.prepareResponseCreated( StoredSeason.builder().seasonId( result.getId() ).build());
    }

    @LogEvent
    public ResponseEntity<BaseResponse> getSeasonsByLeague(Integer leagueId) {
        List<SeasonDto> storedSeasons = seasonServiceDb.getSeasonsByLeague(leagueId);
        SeasonListResponse response = SeasonListResponse.builder()
                .seasons( storedSeasons )
                .build();
        if(storedSeasons.isEmpty()) {
            response.setMessage( InfoMessage.NO_SEASONS_FOUND );
        }
       return  responseResolver.prepareResponse( response );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> initSeason(Integer seasonId) {
        SeasonDto seasonDto = seasonServiceDb.getSingleSeason(seasonId);
        List<MatchDayDto> seasonMatchDays = matchDayServiceDb.getMatchDaysBySeasonId(seasonId);
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague( seasonDto.getLeagueId() );

        InitSeasonResponse response = initSeasonMapper.mapResponse(seasonDto,seasonMatchDays,leagueDto);
        return responseResolver.prepareResponse(response);
    }

    @LogEvent
    public ResponseEntity<BaseResponse> completeSeason(StoredSeason request) {
        seasonServiceDb.completeSeason(request.getSeasonId());
        return responseResolver.prepareResponse( request );
    }
}

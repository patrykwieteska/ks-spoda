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
import pl.spoda.ks.database.service.LeagueServiceDB;
import pl.spoda.ks.database.service.MatchDayServiceDB;
import pl.spoda.ks.database.service.SeasonServiceDB;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonMapper seasonMapper;
    private final SeasonServiceDB seasonServiceDb;
    private final ResponseResolver responseResolver;
    private final LeagueServiceDB leagueServiceDb;
    private final InitSeasonMapper initSeasonMapper;
    private final MatchDayServiceDB matchDayServiceDB;

    @LogEvent
    public ResponseEntity<BaseResponse> createSeason(SeasonRequest request) {
        SeasonDto result = seasonServiceDb.save( seasonMapper.mapSeason( request ) );
        return responseResolver.prepareResponseCreated( StoredSeason.builder().seasonId( result.getId() ).build() );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> getSeasonsByLeague(Integer leagueId) {
        List<SeasonDto> storedSeasons = seasonServiceDb.getSeasonsByLeague( leagueId );
        SeasonListResponse response = SeasonListResponse.builder()
                .seasons( seasonMapper.mapToSeasonList( storedSeasons ) )
                .build();
        if (storedSeasons.isEmpty()) {
            response.setErrorMessage( InfoMessage.NO_SEASONS_FOUND );
        }
        return responseResolver.prepareResponse( response );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> initSeason(Integer seasonId) {
        SeasonDto seasonDto = seasonServiceDb.getSingleSeason( seasonId );
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague( seasonDto.getLeagueId() );
        List<MatchDayDto> matchDayDtos = matchDayServiceDB.getMatchDaysBySeasonId( seasonId );

        InitSeasonResponse response = initSeasonMapper.mapResponse( seasonDto, leagueDto,matchDayDtos );
        return responseResolver.prepareResponse( response );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> completeSeason(StoredSeason request) {
        seasonServiceDb.completeSeason( request.getSeasonId() );
        return responseResolver.prepareResponse( request );
    }
}

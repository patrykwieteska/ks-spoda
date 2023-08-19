package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.api.league.model.StoredLeague;
import pl.spoda.ks.api.league.model.LeagueListResponse;
import pl.spoda.ks.api.league.model.InitLeagueResponse;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.repository.LeagueServiceDb;
import pl.spoda.ks.database.repository.MatchDayServiceDb;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueServiceDb leagueServiceDb;
    private final ResponseResolver responseResolver;
    private final MatchDayServiceDb matchDayServiceDb;

    @LogEvent
    public ResponseEntity<BaseResponse> createLeague(LeagueRequest request) {
        if (leagueServiceDb.isLeagueAlreadyExists( request.getName() )) {
            HttpStatus status = HttpStatus.CONFLICT;
            return responseResolver.prepareResponse( status, InfoMessage.LEAGUE_NAME_ALREADY_TAKEN );
        }

        LeagueDto result = leagueServiceDb.save( leagueMapper.mapLeague( request ) );
        return responseResolver.prepareResponseCreated( StoredLeague.builder().leagueId( result.getId() ).build());
    }

    public ResponseEntity<BaseResponse> getLeagues() {
        List<LeagueDto> storedLeagues = leagueServiceDb.getLeagues();
        LeagueListResponse response = LeagueListResponse.builder()
                .leagues( storedLeagues )
                .build();
        if(storedLeagues.isEmpty()) {
            response.setMessage( InfoMessage.NO_LEAGUES_FOUND );
        }
       return  responseResolver.prepareResponse( response );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> initLeague(Integer leagueId) {
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague(leagueId);
        List<MatchDayDto> leagueMatchDays = matchDayServiceDb.getMatchDaysByLeagueId(leagueId);
        InitLeagueResponse response = InitLeagueResponse.builder()
                .league( leagueDto )
                .matchDays( leagueMatchDays )
                .build();
        return responseResolver.prepareResponse(response);
    }

    @LogEvent
    public ResponseEntity<BaseResponse> completeLeague(StoredLeague request) {
        leagueServiceDb.completeLeague(request.getLeagueId());
        return responseResolver.prepareResponse( request );
    }
}

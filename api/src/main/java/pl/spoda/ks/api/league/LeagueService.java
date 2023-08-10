package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.model.response.BaseResponse;
import pl.spoda.ks.api.commons.ResponseService;
import pl.spoda.ks.api.league.model.request.LeagueRequest;
import pl.spoda.ks.api.league.model.response.LeagueCreatedResponse;
import pl.spoda.ks.api.league.model.response.LeagueListResponse;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.repository.LeagueServiceDb;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueServiceDb leagueServiceDb;
    private final ResponseService responseService;

    @LogEvent
    public ResponseEntity<BaseResponse> createLeague(LeagueRequest request) {
        if (leagueServiceDb.isLeagueAlreadyExists( request.getName() )) {
            HttpStatus status = HttpStatus.CONFLICT;
            return responseService.prepareResponse( status, InfoMessage.LEAGUE_NAME_ALREADY_TAKEN );
        }

        LeagueDto result = leagueServiceDb.save( leagueMapper.mapLeague( request ) );
        return responseService.prepareResponseCreated( LeagueCreatedResponse.builder().leagueId( result.getId() ).build());
    }

    public ResponseEntity<BaseResponse> getLeagues() {
        List<LeagueDto> storedLeagues = leagueServiceDb.getLeagues();
        LeagueListResponse response = LeagueListResponse.builder()
                .leagues( storedLeagues )
                .build();
        if(storedLeagues.isEmpty()) {
            response.setMessage( InfoMessage.NO_LEAGUES_FOUND );
        }
       return  responseService.prepareResponse( response );
    }
}

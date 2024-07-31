package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.league.model.LeagueListResponse;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.api.league.model.StoredLeague;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.service.LeagueServiceDB;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueServiceDB leagueServiceDb;
    private final ResponseResolver responseResolver;

    @LogEvent
    public StoredLeague createLeague(LeagueRequest request) {
        if (leagueServiceDb.isLeagueAlreadyExists( request.getName() )) {
            throw new SpodaApplicationException( InfoMessage.LEAGUE_NAME_ALREADY_TAKEN );
        }
        LeagueDto result = leagueServiceDb.save( leagueMapper.mapLeague( request ) );
        return StoredLeague.builder().leagueId( result.getId() ).build();
    }

    public ResponseEntity<BaseResponse> getLeagues() {
        List<LeagueDto> storedLeagues = leagueServiceDb.getLeagues();
        LeagueListResponse response = LeagueListResponse.builder()
                .leagues( leagueMapper.mapLeagueList( storedLeagues ) )
                .build();
        if (storedLeagues.isEmpty()) {
            response.setErrorMessage( InfoMessage.NO_SEASONS_FOUND );
        }
        return responseResolver.prepareResponse( response );
    }
}

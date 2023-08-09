package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.repository.LeagueServiceDb;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueServiceDb leagueServiceDb;

    public ResponseEntity<LeagueResponse> createLeague(LeagueRequest request) {
        if(leagueServiceDb.isLeagueAlreadyExists(request.getName()))
            return ResponseEntity.status( HttpStatus.CONFLICT ).build();

        LeagueDto result = leagueServiceDb.save( leagueMapper.mapLeague(request));
        return ResponseEntity.ok( LeagueResponse.builder().leagueId( result.getId() ).build());
    }
}

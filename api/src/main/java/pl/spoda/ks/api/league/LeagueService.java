package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.league.model.InitLeagueResponse;
import pl.spoda.ks.api.league.model.LeagueListResponse;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.api.league.model.StoredLeague;
import pl.spoda.ks.api.player.PlayerMapper;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.service.LeagueServiceDB;
import pl.spoda.ks.database.service.PlayerServiceDB;
import pl.spoda.ks.database.service.SeasonServiceDB;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueServiceDB leagueServiceDb;
    private final ResponseResolver responseResolver;
    private final SeasonServiceDB seasonServiceDb;
    private final PlayerServiceDB playerServiceDB;
    private final PlayerMapper playerMapper;

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
                .leagues( leagueMapper.mapLeagueList(storedLeagues) )
                .build();
        if(storedLeagues.isEmpty()) {
            response.setMessage( InfoMessage.NO_SEASONS_FOUND );
        }
       return  responseResolver.prepareResponse( response );
    }

    @LogEvent
    public ResponseEntity<BaseResponse> initLeague(Integer leagueId) {
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague(leagueId);
        List<SeasonDto> leagueSeasons = seasonServiceDb.getSeasonsByLeague(leagueId);
        List<PlayerData> playerList = playerServiceDB.getPlayerListByLeagueId(leagueId).stream()
                .map( playerMapper::mapToPlayerData )
                .toList();
        InitLeagueResponse response = InitLeagueResponse.builder()
                .league( leagueDto )
                .seasons( leagueSeasons )
                .playerList( playerList )
                .build();
        return responseResolver.prepareResponse(response);
    }
}

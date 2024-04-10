package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.league.model.InitLeagueResponse;
import pl.spoda.ks.api.player.PlayerMapper;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.service.LeagueServiceDB;
import pl.spoda.ks.database.service.PlayerServiceDB;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class InitLeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueServiceDB leagueServiceDb;
    private final ResponseResolver responseResolver;
    private final PlayerMapper playerMapper;
    private final PlayerServiceDB playerServiceDB;

    @LogEvent
    public ResponseEntity<BaseResponse> initLeague(Integer leagueId) {
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague( leagueId );
        Set<PlayerData> playersSet = playerMapper.mapToPlayerDataList(
                playerServiceDB.getPlayerListByLeagueId( leagueId )
        );
        InitLeagueResponse response = InitLeagueResponse.builder()
                .league( leagueMapper.mapToLeagueData( leagueDto ) )
                .playerList( playersSet )
                .build();
        return responseResolver.prepareResponse( response );
    }
}

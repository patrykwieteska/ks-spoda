package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.league.model.InitLeagueResponse;
import pl.spoda.ks.api.player.PlayerMapper;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.api.season.SeasonMapper;
import pl.spoda.ks.api.table.TableMapper;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.service.LeagueServiceDB;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class InitLeagueService {

    private final LeagueMapper leagueMapper;
    private final LeagueServiceDB leagueServiceDb;
    private final ResponseResolver responseResolver;

    private final PlayerMapper playerMapper;
    private final TableMapper tableMapper;
    private final SeasonMapper seasonMapper;


    @LogEvent
    public ResponseEntity<BaseResponse> initLeague(Integer leagueId) {
        LeagueDto leagueDto = leagueServiceDb.getSingleLeague( leagueId );
        Set<PlayerData> playersSet = playerMapper.mapToPlayerDataList( leagueDto.getPlayerList() );


        InitLeagueResponse response = InitLeagueResponse.builder()
                .league( leagueMapper.mapToLeagueData( leagueDto ) )
                .seasons( seasonMapper.mapToSeasonList( leagueDto.getSeasonList() ))
                .playerList( playersSet)
                .leagueTable(tableMapper.mapToTableResult(leagueDto.getLeagueTableRowList(),playersSet))
                .build();
        return responseResolver.prepareResponse(response);
    }
}

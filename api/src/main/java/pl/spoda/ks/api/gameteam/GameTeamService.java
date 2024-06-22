package pl.spoda.ks.api.gameteam;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.model.GameTeam;
import pl.spoda.ks.api.match.model.MatchMapper;
import pl.spoda.ks.database.dto.GameTeamDto;
import pl.spoda.ks.database.service.GameTeamServiceDB;

@Service
@RequiredArgsConstructor
public class GameTeamService {

    private final GameTeamServiceDB gameTeamServiceDB;
    private final MatchMapper matchMapper;

    public GameTeam getGameTeam(Integer gameTeamId) {
        GameTeamDto gameTeamById = gameTeamServiceDB.getGameTeamById( gameTeamId );
        return matchMapper.mapToGameTeam( gameTeamById );
    }
}

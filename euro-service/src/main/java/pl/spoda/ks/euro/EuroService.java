package pl.spoda.ks.euro;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.spoda.ks.euro.model.*;
import pl.spoda.ks.euro.model.request.EuroMatchRequest;
import pl.spoda.ks.euro.model.response.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EuroService {

    private final EuroClient euroClient;

    public void addResult(
            List<Player> homePlayers,
            List<Player> awayPlayers,
            Integer homeGoals,
            Integer awayGoals,
            Integer euroMatchId,
            boolean isMatchComplete,
            Integer awayPenaltyGoals,
            Integer homePenaltyGoals,
            String euroId
    ) {
        EuroMatchRequest request = EuroMatchRequest.builder()
                .homePlayers( homePlayers )
                .awayPlayers( awayPlayers )
                .homeGoals( homeGoals )
                .awayGoals( awayGoals )
                .matchNumber( euroMatchId )
                .isFinished( isMatchComplete )
                .penalties( mapToPenaltyKicks(awayPenaltyGoals,homePenaltyGoals) )
                .build();

        euroClient.addResult( euroId,request );
    }

    private PenaltyKicks mapToPenaltyKicks(Integer awayPenaltyGoals, Integer homePenaltyGoals) {
        if(awayPenaltyGoals == null || homePenaltyGoals == null) {
            return null;
        }

         return PenaltyKicks.builder()
                 .homeGoals( homePenaltyGoals )
                 .awayGoals( awayPenaltyGoals )
                 .build();
    }

    public void clearEuroMatch(Integer euroMatchId,String euroId) {
        euroClient.clearResult(euroId,euroMatchId);
    }



    public EuroCalendarResponse getEuroCalendar(String group, String euroId) {
        return euroClient.getEuroCalendar( euroId,TournamentGroup.getByName( group ) );

    }

    public GroupStageTables getGroupsTables( String group, String euroId) {
        return euroClient.getGroupsTables( euroId ,TournamentGroup.getByName( group ));
    }

    public void updatePlayerData(Integer playerId, String alias, String playerImg) {
        euroClient.updateEuroPlayer(Player.builder()
                        .externalPlayerId( playerId )
                        .alias( alias )
                        .imageUrl( playerImg )
                .build());
    }

    public Pair<Integer,Integer> getMatchTeams(Integer matchNumber, String euroId) {
        MatchSquadResponse matchTeams = euroClient.getMatchTeams( euroId,matchNumber );
        return Pair.of( matchTeams.getHomeTeamId(),matchTeams.getAwayTeamId() );
    }

    public ThirdPlacesResponse getThirdPlacesTable(String euroId) {
        return euroClient.getThirdPlacesTable(euroId);
    }

    public CurrentStage getCurrentStage(String euroId) {
        return euroClient.getCurrentStage(euroId);
    }

    public String addNewEuroTournament() {
        return euroClient.addEuroTournament();
    }
}

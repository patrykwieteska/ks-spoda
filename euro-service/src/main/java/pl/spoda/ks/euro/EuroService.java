package pl.spoda.ks.euro;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import pl.spoda.ks.euro.model.*;
import pl.spoda.ks.euro.model.request.MatchRequest;
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
            Integer homePenaltyGoals
    ) {
        MatchRequest request = MatchRequest.builder()
                .homePlayers( homePlayers )
                .awayPlayers( awayPlayers )
                .homeGoals( homeGoals )
                .awayGoals( awayGoals )
                .matchNumber( euroMatchId )
                .isFinished( isMatchComplete )
                .penalties( mapToPenaltyKicks(awayPenaltyGoals,homePenaltyGoals) )
                .build();

        euroClient.addResult( request );
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

    public void clearEuroMatch(Integer euroMatchId) {
        euroClient.clearResult(euroMatchId);
    }



    public EuroCalendarResponse getEuroCalendar(String group) {
        return euroClient.getEuroCalendar( TournamentGroup.getByName( group ) );

    }

    public GroupStageTables getGroupsTables( String group) {
        return euroClient.getGroupsTables( TournamentGroup.getByName( group ) );
    }

    public void updatePlayerData(Integer playerId, String alias, String playerImg) {
        euroClient.updateEuroPlayer(Player.builder()
                        .externalPlayerId( playerId )
                        .alias( alias )
                        .imageUrl( playerImg )
                .build());
    }

    public Pair<Integer,Integer> getMatchTeams(Integer matchNumber) {
        MatchSquadResponse matchTeams = euroClient.getMatchTeams( matchNumber );
        return Pair.of( matchTeams.getHomeTeamId(),matchTeams.getAwayTeamId() );
    }

    public ThirdPlacesResponse getThirdPlacesTable() {
        return euroClient.getThirdPlacesTable();
    }

    public CurrentStage getCurrentStage() {
        TournamentStage currentStage = euroClient.getCurrentStage();
        return CurrentStage.builder()
                .stage( currentStage )
                .description( currentStage.getDescription() )
                .build();
    }
}

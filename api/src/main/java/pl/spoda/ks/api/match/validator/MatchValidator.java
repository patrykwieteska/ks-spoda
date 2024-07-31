package pl.spoda.ks.api.match.validator;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.api.match.model.EditMatchRequest;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.service.LeagueTableServiceDB;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchValidator {

    private final LeagueTableServiceDB leagueTableServiceDB;

    public void validateCreate(CreateMatchRequest request, List<Integer> playerList, Integer leagueId, List<MatchDetailsDto> latestMatchDetails, MatchDayDto matchDay, TeamStructure teamStructure) {
        validatePlayersInLeague( playerList, leagueId );
        validateMatchesInProgress( latestMatchDetails );
        validateMatchDay( matchDay );
        validatePlayers( request.getHomePlayers(), request.getAwayPlayers() );
        validateGameTeams( request.getHomeGameTeamId(), request.getAwayGameTeamId() );
        validateTeamStructure( request, teamStructure );
    }

    private void validateTeamStructure(CreateMatchRequest request, TeamStructure teamStructure) {
        boolean isValid = false;
        String errorMessage = "Nieprawidłowa ilość graczy";
        List<Integer> homePlayers = request.getHomePlayers();
        List<Integer> awayPlayers = request.getAwayPlayers();
        switch (teamStructure) {
            case MIXED -> isValid =
                    !homePlayers.isEmpty() && homePlayers.size() <= 2 && !awayPlayers.isEmpty() && awayPlayers.size() <= 2;

            case DOUBLE -> {
                isValid = homePlayers.size() == 2 && awayPlayers.size() == 2;
                errorMessage = InfoMessage.getMessage( InfoMessage.INVALID_TEAM_STRUCTURE, "2" );
            }
            case SINGLE -> {
                isValid = homePlayers.size() == 1 && awayPlayers.size() == 1;
                errorMessage = InfoMessage.getMessage( InfoMessage.INVALID_TEAM_STRUCTURE, "1" );
            }
        }


        if (!isValid) {
            throw new SpodaApplicationException( errorMessage );
        }


    }

    private void validateGameTeams(Integer homeGameTeamId, Integer awayGameTeamId) {
        if (awayGameTeamId == null && homeGameTeamId == null)
            return;

        if (homeGameTeamId.equals( awayGameTeamId ) && homeGameTeamId!=751) {
            throw new SpodaApplicationException( "Przeciwko sobie nie mogą grać takie same drużyny" );
        }
    }


    private void validatePlayers(
            List<Integer> homePlayers,
            List<Integer> awayPlayers
    ) {
        List<Integer> playerIds = new ArrayList<>();
        playerIds.addAll( homePlayers );
        playerIds.addAll( awayPlayers );

        if (playerIds.stream().distinct().count() != playerIds.size()) {
            throw new SpodaApplicationException( InfoMessage.DUPLICATE_PLAYERS_IN_MATCH_REQUEST );
        }

    }

    private void validateMatchDay(MatchDayDto matchDay) {
        if (Boolean.TRUE.equals( matchDay.getIsFinished() ))
            throw new SpodaApplicationException( InfoMessage.MATCH_DAY_FINISHED );
    }

    private void validatePlayersInLeague(List<Integer> playerList, Integer leagueId) {
        if (!leagueTableServiceDB.arePlayersInTheLeague( playerList, leagueId )) {
            throw new SpodaApplicationException( InfoMessage.NO_PLAYER_IN_THE_LEAGUE );
        }
    }

    private void validateMatchesInProgress(List<MatchDetailsDto> latestMatchDetails) {
        if (latestMatchDetails.stream()
                .anyMatch( MatchDetailsDto::getMatchInProgress )) {
            throw new SpodaApplicationException( InfoMessage.PLAYER_HAS_MATCH_IN_PROGRESS );
        }

    }

    public void validateEdit(MatchDto matchDto, MatchDayDto matchDayDto, EditMatchRequest request) {
        validateMatchIsFinished( matchDto.getIsFinished() );
        validateMatchDay( matchDayDto );
        validateGameTeams( request.getHomeGameTeamId(), request.getAwayGameTeamId() );

    }

    private void validateMatchIsFinished(Boolean isFinished) {
        if (BooleanUtils.isTrue( isFinished )) {
            throw new SpodaApplicationException( InfoMessage.MATCH_FINISHED );
        }
    }
}

package pl.spoda.ks.api.match.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.player.PlayerMapper;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.service.GameTeamServiceDB;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchMapper {

    private final GameTeamServiceDB gameTeamServiceDB;
    private final PlayerMapper playerMapper;

    public Match mapToMatch(MatchDto matchDto) {
        return Match.builder()
                .id( matchDto.getId() )
                .isFinished( matchDto.getIsFinished() )
                .matchTime( matchDto.getMatchTime() )
                .awayGoals( matchDto.getAwayGoals() )
                .homeGoals( matchDto.getHomeGoals() )
                .homeTeam( mapToMatchTeam( matchDto.getHomeTeam() ) )
                .awayTeam( mapToMatchTeam( matchDto.getAwayTeam() ) )
                .build();
    }

    private MatchTeam mapToMatchTeam(MatchTeamDto matchTeam) {

        return MatchTeam.builder()
                .gameTeam( mapGameTeam( matchTeam ) )
                .matchPlayers( playerMapper.mapToPlayerDataList( matchTeam.getTeamPlayers() ) )
                .build();
    }

    private GameTeam mapGameTeam(MatchTeamDto matchTeam) {
        return Optional.ofNullable( matchTeam.getGameTeamId() )
                .map( gameTeamServiceDB::getGameTeamById )
                .map(this::mapToGameTeam )
                .orElse( null );
    }

    public GameTeam mapToGameTeam(GameTeamDto gameTeamDto) {

        return GameTeam.builder()
                .id( gameTeamDto.getId() )
                .name( gameTeamDto.getName() )
                .badgeImg( gameTeamDto.getBadgeImg() )
                .build();
    }

    public MatchDto mapToNewDto(
            CreateMatchRequest createMatchRequest,
            List<MatchDayTableDto> matchDayTable
    ) {
        return MatchDto.builder()
                .awayGoals( createMatchRequest.getAwayGoals() )
                .homeGoals( createMatchRequest.getHomeGoals() )
                .matchDayId( createMatchRequest.getMatchDayId() )
                .matchTime( LocalDateTime.now() )
                .isFinished( false )
                .homeTeam( mapMatchTeam( createMatchRequest.getHomePlayers(), matchDayTable, createMatchRequest.getHomeGameTeamId() ) )
                .awayTeam( mapMatchTeam( createMatchRequest.getAwayPlayers(), matchDayTable, createMatchRequest.getAwayGameTeamId() ) )
                .build();
    }

    private MatchTeamDto mapMatchTeam(
            @NotEmpty List<Integer> homePlayers,
            List<MatchDayTableDto> matchDayTable,
            Integer gameTeamId
    ) {
        return MatchTeamDto.builder()
                .teamPlayers( getPlayersFromMatchDayTable( matchDayTable, homePlayers ) )
                .gameTeamId( gameTeamId )
                .build();
    }

    private Set<PlayerDto> getPlayersFromMatchDayTable(
            List<MatchDayTableDto> matchDayTable,
            List<Integer> homePlayers
    ) {

        return matchDayTable.stream()
                .map( MatchDayTableDto::getPlayer )
                .filter( player -> homePlayers.contains( player.getId() ) )
                .collect( Collectors.toSet() );
    }

    public List<Match> mapToMatchList(List<MatchDto> leagueMatches) {
        return leagueMatches.stream()
                .map( this::mapToMatch )
                .toList();
    }

    public MatchDto updateMatch(MatchDto matchDto, EditMatchRequest request) {
        matchDto.setAwayGoals( request.getAwayGoals() );
        matchDto.setHomeGoals( request.getHomeGoals() );
        matchDto.setIsFinished( Optional.ofNullable( request.getIsComplete() ).orElse( matchDto.getIsFinished() ) );
        matchDto.getAwayTeam().setGameTeamId( Optional.ofNullable( request.getAwayGameTeamId() ).orElse( matchDto.getAwayTeam().getGameTeamId() ) );
        matchDto.getHomeTeam().setGameTeamId( Optional.ofNullable( request.getHomeGameTeamId() ).orElse( matchDto.getHomeTeam().getGameTeamId() ) );
        return matchDto;
    }
}

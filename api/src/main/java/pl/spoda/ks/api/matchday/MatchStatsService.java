package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.gameteam.GameTeamService;
import pl.spoda.ks.api.match.enums.MatchResult;
import pl.spoda.ks.api.match.matchdetails.MatchResultService;
import pl.spoda.ks.api.matchday.model.MatchPlayers;
import pl.spoda.ks.api.matchday.model.MatchStats;
import pl.spoda.ks.api.matchday.model.MatchStatsResult;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.dto.MatchTeamDto;
import pl.spoda.ks.database.dto.PlayerDto;

import java.util.*;

import static pl.spoda.ks.api.matchday.MatchStatsService.TeamSite.AWAY;
import static pl.spoda.ks.api.matchday.MatchStatsService.TeamSite.HOME;

@Service
@RequiredArgsConstructor
public class MatchStatsService {

    private static final Logger log = LoggerFactory.getLogger( MatchStatsService.class );
    private final GameTeamService gameTeamService;
    private final MatchResultService matchResultService;

    public MatchStats fillByGameStats(
            String playerAlias,
            List<MatchDto> matches
    ) {
        List<MatchDto> playerMatches = getPlayerMatches( playerAlias, matches );
        return MatchStats.builder()
                .games( playerMatches.size() )
                .lastMatch( getLastPlayerMatch( playerMatches, playerAlias ) )
                .commonOpponents( getMostCommonOpponent( playerMatches, playerAlias ) )
                .commonTeammates( getCommonTeammate( playerMatches, playerAlias ) )
                .biggestFailure( getBiggestFailure( playerMatches, playerAlias ) )
                .greatestVictory( getGreatestVictory( playerMatches, playerAlias ) )
                .build();
    }

    private MatchStatsResult getBiggestFailure(List<MatchDto> playerMatches, String playerAlias) {
        List<MatchStatsResult> loses = new ArrayList<>();
        playerMatches
                .forEach( match -> {
                    TeamSite teamSite = getTeamSite( playerAlias, match );
                    MatchResult playerResult = calculatePlayerResult( match, teamSite );
                    if (playerResult.equals( MatchResult.LOSE )) {
                        addResult( teamSite, match, loses, playerAlias );
                    }
                } );

        return loses.stream().max( Comparator.comparing( MatchStatsResult::getGoalsDiff ))
                .orElse( null );
    }

    private MatchStatsResult getGreatestVictory(List<MatchDto> playerMatches, String playerAlias) {
        List<MatchStatsResult> wins = new ArrayList<>();
        playerMatches
                .forEach( match -> {
                    TeamSite teamSite = getTeamSite( playerAlias, match );
                    MatchResult playerResult = calculatePlayerResult( match, teamSite );
                    if (playerResult.equals( MatchResult.WIN )) {
                        addResult( teamSite, match, wins, playerAlias );
                    }
                } );

        return wins.stream()
                .max( Comparator.comparing( MatchStatsResult::getGoalsDiff ) )
                .orElse( null );
    }

    private MatchResult calculatePlayerResult(MatchDto match, TeamSite teamSite) {
        MatchResult result;
        if (teamSite.equals( HOME )) {
            result = matchResultService.getResult( match.getHomeGoals(), match.getAwayGoals() );
        } else {
            result = matchResultService.getResult( match.getAwayGoals(), match.getHomeGoals() );
        }

        return result;
    }

    private void addResult(TeamSite teamSite, MatchDto match, List<MatchStatsResult> loses, String playerAlias) {
        if (TeamSite.HOME.equals( teamSite )) {
            loses.add( MatchStatsResult.builder()
                    .teamGoals( match.getHomeGoals() )
                    .team( gameTeamService.getGameTeam( match.getHomeTeam().getGameTeamId() ) )
                    .teammate( getTeammate( playerAlias, match.getHomeTeam().getTeamPlayers() ) )
                    .opponentGoals( match.getAwayGoals() )
                    .opponentTeam( gameTeamService.getGameTeam( match.getAwayTeam().getGameTeamId() ) )
                    .opponents( getOpponents( match.getAwayTeam().getTeamPlayers() ) )
                    .matchDate( match.getMatchTime() )
                    .goalsDiff( Math.abs(  match.getHomeGoals() - match.getAwayGoals()) )
                    .build() );
        } else {
            loses.add( MatchStatsResult.builder()
                    .teamGoals( match.getAwayGoals() )
                    .team( gameTeamService.getGameTeam( match.getAwayTeam().getGameTeamId() ) )
                    .teammate( getTeammate( playerAlias, match.getAwayTeam().getTeamPlayers() ) )
                    .opponentGoals( match.getHomeGoals() )
                    .opponentTeam( gameTeamService.getGameTeam( match.getHomeTeam().getGameTeamId() ) )
                    .opponents( getOpponents( match.getHomeTeam().getTeamPlayers() ) )
                    .matchDate( match.getMatchTime() )
                    .goalsDiff( Math.abs( match.getAwayGoals() -  match.getHomeGoals() ))
                    .build() );
        }

    }

    private MatchPlayers getCommonTeammate(List<MatchDto> playerMatches, String playerAlias) {
        Map<String, Integer> teammates = new HashMap<>();
        playerMatches
                .forEach( match -> {
                    TeamSite teamSite = getTeamSite( playerAlias, match );
                    if (teamSite.equals( TeamSite.HOME )) {
                        putPlayers( match.getHomeTeam(), teammates, playerAlias );
                    } else {
                        putPlayers( match.getAwayTeam(), teammates, playerAlias );
                    }
                } );

        Integer max = teammates.values().stream()
                .mapToInt( x -> x )
                .max()
                .orElse( 0 );

        if (max.equals( 0 )) {
            return null;
        }

        List<String> playerAliases = teammates.entrySet().stream()
                .filter( x -> x.getValue().equals( max ) )
                .map( Map.Entry::getKey )
                .distinct().toList();

        return MatchPlayers.builder()
                .players( playerAliases )
                .gamesCount( max )
                .build();

    }

    private TeamSite getTeamSite(String playerAlias, MatchDto match) {
        return isPlayerInTeam( playerAlias, match.getHomeTeam() ) ? HOME : AWAY;
    }

    private MatchPlayers getMostCommonOpponent(List<MatchDto> playerMatches, String playerAlias) {
        Map<String, Integer> opponents = new HashMap<>();
        playerMatches
                .forEach( match -> {
                    TeamSite teamSite = getTeamSite( playerAlias, match );
                    if (teamSite.equals( TeamSite.HOME )) {
                        putPlayers( match.getAwayTeam(), opponents, playerAlias );
                    } else {
                        putPlayers( match.getHomeTeam(), opponents, playerAlias );
                    }
                } );

        Integer max = opponents.values().stream()
                .mapToInt( x -> x )
                .max()
                .orElse( 0 );

        if (max.equals( 0 )) {
            return null;
        }

        List<String> playerAliases = opponents.entrySet().stream()
                .filter( x -> x.getValue().equals( max ) )
                .map( Map.Entry::getKey )
                .distinct().toList();

        return MatchPlayers.builder()
                .players( playerAliases )
                .gamesCount( max )
                .build();


    }

    private void putPlayers(MatchTeamDto match, Map<String, Integer> playerMatchesCount, String playerAlias) {
        match.getTeamPlayers().stream()
                .map( PlayerDto::getAlias )
                .filter( alias -> !alias.equals( playerAlias ) )
                .forEach( alias ->
                        Optional.ofNullable( playerMatchesCount.get( alias ) ).ifPresentOrElse(
                                x -> playerMatchesCount.put( alias, x + 1 ), () -> playerMatchesCount.put( alias,1 )
                        )
                );
    }

    private MatchStatsResult getLastPlayerMatch(List<MatchDto> playerMatches, String playerAlias) {
        MatchDto lastMatch = getPlayerMatchesSortedByDate( playerMatches )
                .stream()
                .findFirst()
                .orElse( null );

        if (lastMatch == null) {
            return null;
        }

        TeamSite teamSite = getTeamSite( playerAlias, lastMatch );
        return mapToMatchResult( lastMatch, teamSite, playerAlias );
    }

    private static List<MatchDto> getPlayerMatchesSortedByDate(List<MatchDto> playerMatches) {
        return playerMatches.stream()
                .sorted( Comparator.comparing( MatchDto::getMatchTime ).reversed() ).toList();
    }

    private MatchStatsResult mapToMatchResult(MatchDto matchDto, TeamSite teamSite, String playerAlias) {
        return switch (teamSite) {
            case HOME -> MatchStatsResult.builder()
                    .matchDate( matchDto.getMatchTime() )
                    .opponentGoals( matchDto.getAwayGoals() )
                    .teamGoals( matchDto.getHomeGoals() )
                    .teammate( getTeammate( playerAlias, matchDto.getHomeTeam().getTeamPlayers() ) )
                    .opponents( getOpponents( matchDto.getAwayTeam().getTeamPlayers() ) )
                    .team( gameTeamService.getGameTeam( matchDto.getHomeTeam().getGameTeamId() ) )
                    .opponentTeam( gameTeamService.getGameTeam( matchDto.getAwayTeam().getGameTeamId() ) )
                    .goalsDiff( Math.abs(matchDto.getHomeGoals() - matchDto.getAwayGoals()) )
                    .build();
            case AWAY -> MatchStatsResult.builder()
                    .matchDate( matchDto.getMatchTime() )
                    .opponentGoals( matchDto.getHomeGoals() )
                    .teamGoals( matchDto.getAwayGoals() )
                    .teammate( getTeammate( playerAlias, matchDto.getAwayTeam().getTeamPlayers() ) )
                    .opponents( getOpponents( matchDto.getHomeTeam().getTeamPlayers() ) )
                    .team( gameTeamService.getGameTeam( matchDto.getAwayTeam().getGameTeamId() ) )
                    .opponentTeam( gameTeamService.getGameTeam( matchDto.getHomeTeam().getGameTeamId() ) )
                    .goalsDiff( Math.abs(matchDto.getAwayGoals() - matchDto.getHomeGoals()) )
                    .build();
        };
    }

    private List<String> getOpponents(Set<PlayerDto> teamPlayers) {
        return teamPlayers.stream()
                .map( PlayerDto::getAlias )
                .toList();
    }

    private String getTeammate(String playerAlias, Set<PlayerDto> teamPlayers) {
        return teamPlayers.stream()
                .map( PlayerDto::getAlias )
                .filter( alias -> !alias.equals( playerAlias ) )
                .findFirst()
                .orElse( null );
    }

    private List<MatchDto> getPlayerMatches(String playerAlias, List<MatchDto> matchesByMatchDay) {
        return matchesByMatchDay.stream()
                .filter( matchDto -> isPlayerInMatch( playerAlias, matchDto ) )
                .toList();
    }

    private boolean isPlayerInMatch(String playerAlias, MatchDto matchDto) {
        return isPlayerInTeam( playerAlias, matchDto.getAwayTeam() )
                || isPlayerInTeam( playerAlias, matchDto.getHomeTeam() );
    }

    private boolean isPlayerInTeam(String playerAlias, MatchTeamDto team) {
        return team.getTeamPlayers().stream().map( PlayerDto::getAlias ).toList().contains( playerAlias );
    }


    enum TeamSite {
        HOME,
        AWAY
    }
}

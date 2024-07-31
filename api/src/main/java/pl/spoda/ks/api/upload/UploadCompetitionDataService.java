package pl.spoda.ks.api.upload;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.league.LeagueService;
import pl.spoda.ks.api.league.enums.LeagueType;
import pl.spoda.ks.api.league.enums.TeamStructure;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.api.league.model.StoredLeague;
import pl.spoda.ks.api.matchday.MatchDayService;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.api.matchday.model.MatchDayStored;
import pl.spoda.ks.api.player.PlayerMapper;
import pl.spoda.ks.api.season.SeasonService;
import pl.spoda.ks.api.season.enums.RatingType;
import pl.spoda.ks.api.season.model.SeasonRequest;
import pl.spoda.ks.api.season.model.StoredSeason;
import pl.spoda.ks.database.entity.MatchDetails;
import pl.spoda.ks.database.service.PlayerServiceDB;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadCompetitionDataService {

    public static final String EMPTY_BODY_AFTER_CREATED_LEAGUE = "Empty body after created league";
    public static final String EMPTY_BODY_AFTER_CREATED_SEASON = "Empty body after created season";
    public static final String EMPTY_BODY_AFTER_CREATED_MATCH_DAY = "Empty body after created matchDay";
    private final LeagueService leagueService;
    private final SeasonService seasonService;
    private final MatchDayService matchDayService;
    private final PlayerServiceDB playerServiceDB;
    private final PlayerMapper playerMapper;

    public Integer prepareLeagueId(UploadRequest request, MatchDetails lastMatchData) {
        if (lastMatchData == null || BooleanUtils.isTrue( request.getIsNewLeague() )) {
            StoredLeague storedLeague = leagueService.createLeague( prepareLeagueRequest( request ) );
            return Optional.ofNullable( storedLeague ).map( StoredLeague::getLeagueId ).orElseThrow( () -> new IllegalArgumentException( EMPTY_BODY_AFTER_CREATED_LEAGUE ) );
        }
        return lastMatchData.getLeagueId();
    }

    private LeagueRequest prepareLeagueRequest(UploadRequest request) {
        return LeagueRequest.builder()
                .playerList( playerMapper.mapToPlayerDataList( playerServiceDB.getPlayerList() ).stream().toList() )
                .type( LeagueType.SEASON )
                .name( request.getLeagueName() )
                .logoUrl( request.getLeagueImage() )
                .teamStructure( TeamStructure.DOUBLE )
                .isPrivate( false )
                .startDate( request.getLeagueStartDate() )
                .build();
    }

    public Integer prepareSeasonId(UploadRequest uploadRequest, MatchDetails lastMatchData, Integer leagueId) {
        if (lastMatchData == null || BooleanUtils.isTrue( uploadRequest.getIsNewSeason() )) {
            StoredSeason response = seasonService.createSeason( prepareSeasonRequest( uploadRequest, leagueId ) );
            return Optional.ofNullable( response ).map( StoredSeason::getSeasonId ).orElseThrow( () -> new IllegalArgumentException( EMPTY_BODY_AFTER_CREATED_SEASON ) );
        }

        return lastMatchData.getSeasonId();
    }

    private SeasonRequest prepareSeasonRequest(UploadRequest request, Integer leagueId) {
        return SeasonRequest.builder()
                .isEuro( request.getEuroMatchId() != null )
                .leagueId( leagueId )
                .ratingType( RatingType.SINGLE )
                .pointCountingMethod( request.getPointCountingMethod() )
                .startDate( request.getSeasonStartDate() )
                .seasonName( request.getSeasonName() )
                .image( request.getSeasonImage() )
                .isEuro( request.getIsEuro() )
                .matchWeightIndex( request.getMatchWeightIndex() )
                .build();
    }

    public Integer prepareMatchDayId(UploadRequest uploadRequest, MatchDetails lastMatchData, Integer seasonId) {
        if (lastMatchData == null || BooleanUtils.isTrue( uploadRequest.getIsNewMatchDay() )) {
            finishCurrentMatchDay( lastMatchData, seasonId ); // zamknięcie poprzedniej kolejki gdy kontynuowany jest
            // sezon
            MatchDayStored response = matchDayService.createMatchDay( prepareMatchDayRequest( seasonId,
                    uploadRequest ) );
            return Optional.ofNullable( response ).map( MatchDayStored::getMatchDayId ).orElseThrow( () -> new IllegalArgumentException( EMPTY_BODY_AFTER_CREATED_MATCH_DAY ) );
        }

        return lastMatchData.getMatchDayId();
    }

    private void finishCurrentMatchDay(MatchDetails lastMatchData, Integer newestSeasonId) {
        if (lastMatchData == null) {
            return;
        }

        Integer matchDayId = lastMatchData.getMatchDayId();
        Integer seasonId = lastMatchData.getSeasonId();

        if (!seasonId.equals( newestSeasonId )) {
            return;
        }

        matchDayService.completeMatchDay( MatchDayStored.builder().matchDayId( matchDayId ).build() );
    }

    private CreateMatchDayRequest prepareMatchDayRequest(Integer seasonId, UploadRequest request) {
        return CreateMatchDayRequest.builder()
                .seasonId( seasonId )
                .matchDayDate( request.getMatchDayStartDate() )
                .location( request.getMatchDayLocation() )
                .build();
    }
}

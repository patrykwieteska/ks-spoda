package pl.spoda.ks.api.upload;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.MatchService;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.api.match.model.EditMatchRequest;
import pl.spoda.ks.api.match.model.MatchCreated;
import pl.spoda.ks.api.match.model.PenaltyKicks;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.entity.MatchDetails;
import pl.spoda.ks.database.repository.MatchDetailsRepository;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadService {


    private final MatchDetailsRepository matchDetailsRepository;
    private final UploadCompetitionDataService uploadCompetitionDataService;
    private final MatchService matchService;
    private final UploadMatchRequestMapper uploadMatchRequestMapper;

    @Transactional
    public void uploadMatches() {
        List<UploadRequest> request;
        try {
            request = uploadMatchRequestMapper.prepareMatchRequestList();
        } catch (IOException e) {
            throw new SpodaApplicationException( "Error while mapping upload request",e );
        }

            request.forEach( this::tryUpload );
    }

    private void tryUpload(UploadRequest uploadReq) {
        try {
            uploadSingleMatch( uploadReq );
        } catch (Exception e) {
            throw new SpodaApplicationException( String.format( "Error for upload number: %s", uploadReq.getUploadId() ), e );
        }
    }

    void uploadSingleMatch(UploadRequest request) {
        MatchDetails lastMatchData = matchDetailsRepository.findLastMatchDetails().orElse( null );
        Integer leagueId = uploadCompetitionDataService.prepareLeagueId( request, lastMatchData );
        Integer seasonId = uploadCompetitionDataService.prepareSeasonId( request, lastMatchData, leagueId );
        Integer matchDayId = uploadCompetitionDataService.prepareMatchDayId( request, lastMatchData, seasonId );

        CreateMatchRequest matchRequest = prepareMatchRequest( matchDayId, request );
        EditMatchRequest editMatchRequest = prepareEditMatchRequest( request );

        MatchCreated matchCreated = matchService.createMatch( matchRequest );
        matchService.editMatch( matchCreated.getMatchId(), editMatchRequest );
    }

    private EditMatchRequest prepareEditMatchRequest(UploadRequest request) {
        return EditMatchRequest.builder()
                .euroMatchId( request.getEuroMatchId() )
                .isComplete( true )
                .penalties( preparePenalties( request ) )
                .awayGameTeamId( request.getAwayTeamId() )
                .homeGameTeamId( request.getHomeTeamId() )
                .homeGoals( request.getHomeGoals() )
                .awayGoals( request.getAwayGoals() )
                .matchCommentary( request.getMatchCommentary() )
                .build();
    }

    private PenaltyKicks preparePenalties(UploadRequest request) {
        if (BooleanUtils.isNotTrue( request.getArePenalties() )) {
            return null;
        }
        return PenaltyKicks.builder()
                .homeGoals( request.getPenaltiesHome() )
                .awayGoals( request.getPenaltiesAway() )
                .build();
    }

    private CreateMatchRequest prepareMatchRequest(Integer matchDayId, UploadRequest request) {
        return CreateMatchRequest.builder()
                .euroMatchId( request.getEuroMatchId() )
                .matchDayId( matchDayId )
                .awayGoals( request.getAwayGoals() )
                .awayPlayers( request.getAwayPlayers() )
                .awayGameTeamId( request.getAwayTeamId() )
                .isPlayOffMatch( request.getIsPlayOff() )
                .homeGoals( request.getHomeGoals() )
                .homePlayers( request.getHomePlayers() )
                .homeGameTeamId( request.getHomeTeamId() )
                .matchTime( request.getMatchTime() )
                .build();

    }
}

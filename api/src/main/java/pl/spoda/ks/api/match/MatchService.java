package pl.spoda.ks.api.match;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.match.model.Match;
import pl.spoda.ks.api.match.model.MatchListResponse;
import pl.spoda.ks.api.match.model.MatchMapper;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.service.MatchServiceDB;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchMapper matchMapper;
    private final MatchServiceDB matchServiceDB;
    private final ResponseResolver responseResolver;

    public ResponseEntity<BaseResponse> getMatchesByLeague(Integer leagueId) {
        List<MatchDto> leagueMatches = matchServiceDB.findMatchesByLeague(leagueId);
        List<Match> matches = matchMapper.mapToMatchResponseList( leagueMatches );
        return responseResolver.prepareResponse( MatchListResponse.builder()
                        .leagueId( leagueId )
                        .matchList( matches )
                .build() );
    }

}

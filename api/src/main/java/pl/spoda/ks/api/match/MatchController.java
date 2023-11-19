package pl.spoda.ks.api.match;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.spoda.ks.api.commons.BaseResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping()
    public ResponseEntity<BaseResponse> getMatchesByLeague(
            @RequestParam(name="leagueId") Integer leagueId
    ) {
       return matchService.getMatchesByLeague(leagueId);
    }
}

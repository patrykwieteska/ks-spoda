package pl.spoda.ks.api.match;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.api.match.model.EditMatchRequest;

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

    @PostMapping("/create-match")
    public ResponseEntity<BaseResponse> createMatch(
            @RequestBody @Valid CreateMatchRequest createMatchRequest
    ) {
         return matchService.createMatch( createMatchRequest );
    }

    @PostMapping("/edit-match")
    public ResponseEntity<BaseResponse> editMatch(
            @RequestParam(value = "matchId") Integer matchId,
            @RequestBody @Valid EditMatchRequest createMatchRequest
    ) {
        return matchService.editMatch( matchId,createMatchRequest );
    }

    @DeleteMapping("/remove-match")
    public ResponseEntity<BaseResponse> removeMatch(
            @RequestParam(value = "matchId") Integer matchId
    ) {
        return matchService.removeMatch( matchId );
    }
}

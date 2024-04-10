package pl.spoda.ks.api.match;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.match.model.CreateMatchRequest;
import pl.spoda.ks.api.match.model.EditMatchRequest;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/getLeagueMatches")
    @CrossOrigin
    public ResponseEntity<BaseResponse> getMatchesByLeague(
            @RequestParam(name="leagueId") Integer leagueId
    ) {
       return matchService.getMatchesByLeague(leagueId);
    }

    @GetMapping("/getMatchDayMatches")
    @CrossOrigin
    @LogEvent
    public ResponseEntity<BaseResponse> getMatchesByMatchDay(
            @RequestParam(name="matchDayId") Integer matchDayId
    ) {
        return matchService.getMatchesByMatchDay(matchDayId);
    }


    @PostMapping("/create-match")
    @CrossOrigin
    @LogEvent
    public ResponseEntity<BaseResponse> createMatch(
            @RequestBody @Valid CreateMatchRequest createMatchRequest
    ) {
         return matchService.createMatch( createMatchRequest );
    }

    @PostMapping("/edit-match")
    @CrossOrigin
    @LogEvent
    public ResponseEntity<BaseResponse> editMatch(
            @RequestParam(value = "matchId") Integer matchId,
            @RequestBody @Valid EditMatchRequest createMatchRequest
    ) {
        return matchService.editMatch( matchId,createMatchRequest );
    }

    @DeleteMapping("/remove-match")
    @CrossOrigin
    @LogEvent
    public ResponseEntity<BaseResponse> removeMatch(
            @RequestParam(value = "matchId") Integer matchId
    ) {
        return matchService.removeMatch( matchId );
    }

    @GetMapping("/initGameTeams")
    @CrossOrigin
    public ResponseEntity<BaseResponse> initGameTeams(
            @RequestParam(name="matchDayId") Integer matchDayId
    ) {
        return matchService.initGameTeams(matchDayId);
    }
}

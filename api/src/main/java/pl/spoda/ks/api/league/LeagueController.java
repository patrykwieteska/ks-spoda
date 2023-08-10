package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.model.response.BaseResponse;
import pl.spoda.ks.api.league.model.request.LeagueRequest;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    @PostMapping
    @LogEvent
    public ResponseEntity<BaseResponse> createLeague(
            @RequestBody LeagueRequest request
    ) {
        return leagueService.createLeague(request);
    }

    @GetMapping
    @LogEvent
    public ResponseEntity<BaseResponse> getLeagues() {
        return leagueService.getLeagues();
    }
}

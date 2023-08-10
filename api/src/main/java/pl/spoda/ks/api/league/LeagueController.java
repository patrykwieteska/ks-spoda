package pl.spoda.ks.api.league;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/leagues")
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

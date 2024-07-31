package pl.spoda.ks.api.league;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.api.league.model.StoredLeague;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/leagues")
public class LeagueController {

    private final LeagueService leagueService;
    private final InitLeagueService initLeagueService;
    private final ResponseResolver responseResolver;

    @CrossOrigin
    @PostMapping
    @LogEvent
    public ResponseEntity<BaseResponse> createLeague(
            @RequestBody @Valid LeagueRequest request
    ) {
        StoredLeague league = leagueService.createLeague( request );
        return responseResolver.prepareResponseCreated( StoredLeague.builder().leagueId( league.getLeagueId() ).build());
    }

    @GetMapping
    @LogEvent
    @CrossOrigin
    public ResponseEntity<BaseResponse> getLeagues() {
        return leagueService.getLeagues();
    }

    @GetMapping("init/{id}")
    @CrossOrigin
    @LogEvent
    public ResponseEntity<BaseResponse> initLeague(
            @PathVariable(name = "id") Integer leagueId
    ) {
        return initLeagueService.initLeague(leagueId);
    }
}

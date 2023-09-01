package pl.spoda.ks.api.season;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.season.model.StoredSeason;
import pl.spoda.ks.api.season.model.SeasonRequest;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/seasons")
public class SeasonController {

    private final SeasonService seasonService;

    @PostMapping
    @LogEvent
    public ResponseEntity<BaseResponse> createSeason(
            @RequestBody SeasonRequest request
    ) {
        return seasonService.createLeague( request );
    }

    @GetMapping()
    @LogEvent
    public ResponseEntity<BaseResponse> getSeasonsByLeague(
            @RequestParam(name = "leagueId") Integer leagueId
    ) {
        return seasonService.getSeasonsByLeague(leagueId);
    }

    @GetMapping("init/{id}")
    @LogEvent
    public ResponseEntity<BaseResponse> initSeason(
            @PathVariable(name = "id") Integer seasonId
    ) {
        return seasonService.initSeason( seasonId );
    }

    @PutMapping("/complete")
    @LogEvent
    public ResponseEntity<BaseResponse> completeSeason(
            @RequestBody StoredSeason request
    ) {
        return seasonService.completeSeason( request );
    }
}

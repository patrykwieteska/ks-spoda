package pl.spoda.ks.api.season;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.commons.ResponseResolver;
import pl.spoda.ks.api.season.model.StoredSeason;
import pl.spoda.ks.api.season.model.SeasonRequest;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/seasons")
public class SeasonController {

    private final SeasonService seasonService;
    private final ResponseResolver responseResolver;

    @CrossOrigin
    @PostMapping
    @LogEvent
    public ResponseEntity<BaseResponse> createSeason(
            @RequestBody @Valid SeasonRequest request
    ) {
        StoredSeason newSeason = seasonService.createSeason( request );
        return responseResolver.prepareResponseCreated( StoredSeason.builder().seasonId( newSeason.getSeasonId() ).build() );

    }

    @CrossOrigin
    @GetMapping()
    @LogEvent
    public ResponseEntity<BaseResponse> getSeasonsByLeague(
            @RequestParam(name = "leagueId") Integer leagueId
    ) {
        return seasonService.getSeasonsByLeague(leagueId);
    }

    @GetMapping("init/{id}")
    @LogEvent
    @CrossOrigin
    public ResponseEntity<BaseResponse> initSeason(
            @PathVariable(name = "id") Integer seasonId
    ) {
        return seasonService.initSeason( seasonId );
    }

    @CrossOrigin
    @PutMapping("/complete")
    @LogEvent
    public ResponseEntity<BaseResponse> completeSeason(
            @RequestBody StoredSeason request
    ) {
        return seasonService.completeSeason( request );
    }
}

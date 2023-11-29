package pl.spoda.ks.api.table;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.league.LeagueService;
import pl.spoda.ks.api.league.model.LeagueRequest;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/table")
public class TableController {

    private final LeagueService leagueService;
    private final TableService tableService;

    @CrossOrigin
    @PostMapping
    @LogEvent
    public ResponseEntity<BaseResponse> createLeague(
            @RequestBody @Valid LeagueRequest request
    ) {
        return leagueService.createLeague( request );
    }

    @GetMapping("/initLeagueTable")
    @LogEvent
    @CrossOrigin
    public ResponseEntity<BaseResponse> initLeagueTable(
            @RequestParam(name = "leagueId") Integer leagueId
    ) {
        if (leagueId == null) {
            throw new SpodaApplicationException( "leagueId is null" );
        }
        return tableService.getLeagueTable( leagueId );
    }

    @GetMapping("/initSeasonTable")
    @LogEvent
    @CrossOrigin
    public ResponseEntity<BaseResponse> initSeasonTable(
            @RequestParam(name = "seasonId") Integer seasonId
    ) {
        if (seasonId == null) {
            throw new SpodaApplicationException( "season is null" );
        }
        return tableService.getSeasonTable( seasonId );
    }
}

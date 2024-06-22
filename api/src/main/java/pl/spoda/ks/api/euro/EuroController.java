package pl.spoda.ks.api.euro;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.match.EuroMatchService;
import pl.spoda.ks.api.season.SeasonService;
import pl.spoda.ks.euro.EuroService;
import pl.spoda.ks.euro.model.CurrentStage;
import pl.spoda.ks.euro.model.EuroMatch;
import pl.spoda.ks.euro.model.response.EuroCalendarResponse;
import pl.spoda.ks.euro.model.response.GroupStageTables;
import pl.spoda.ks.euro.model.response.ThirdPlacesResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/euro")
public class EuroController {

    private final EuroService euroService;
    private final EuroMatchService euroMatchService;
    private final SeasonService seasonService;

    @GetMapping("/calendar/{seasonId}")
    @CrossOrigin
    public EuroCalendarResponse getEuroCalendar(
            @PathVariable Integer seasonId,
            @RequestParam(required = false) String stage
    ) {
        String euroId = seasonService.getEuroId( seasonId );
        return euroService.getEuroCalendar( stage, euroId );
    }

    @GetMapping("/tables/{seasonId}")
    @CrossOrigin
    public GroupStageTables getEuroGroupData(
            @PathVariable int seasonId,
            @RequestParam(required = false) String group
    ) {
        String euroId = seasonService.getEuroId( seasonId );
        return euroService.getGroupsTables( group, euroId );
    }

    @GetMapping("/next-match/{seasonId}")
    @CrossOrigin
    public EuroMatch getNextMatch(
            @PathVariable int seasonId
    ) {
        String euroId = seasonService.getEuroId( seasonId );
        return euroMatchService.getNextEuroMatch( euroId );
    }

    @CrossOrigin
    @GetMapping("/third-places/{seasonId}")
    ThirdPlacesResponse getThirdPlacesTable(
            @PathVariable int seasonId
    ) {
        String euroId = seasonService.getEuroId( seasonId );
        return euroService.getThirdPlacesTable( euroId );
    }

    @GetMapping("/group-matches/{seasonId}")
    @CrossOrigin
    public EuroMatchSchedule getPlayedMatches(
            @PathVariable int seasonId,
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) Integer limit
    ) {
        if (limit == null) {
            limit = 0;
        }
        String euroId = seasonService.getEuroId( seasonId );
        return euroMatchService.getPlayedMatches( stage, limit, euroId );
    }

    @GetMapping("/current-stage/{seasonId}")
    @CrossOrigin
    public CurrentStage getCurrentStage(
            @PathVariable int seasonId
    ) {
        String euroId = seasonService.getEuroId( seasonId );
        return euroService.getCurrentStage( euroId );
    }
}

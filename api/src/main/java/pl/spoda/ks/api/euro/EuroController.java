package pl.spoda.ks.api.euro;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.match.EuroMatchService;
import pl.spoda.ks.euro.EuroService;
import pl.spoda.ks.euro.model.EuroMatch;
import pl.spoda.ks.euro.model.CurrentStage;
import pl.spoda.ks.euro.model.response.EuroCalendarResponse;
import pl.spoda.ks.euro.model.response.GroupStageTables;
import pl.spoda.ks.euro.model.response.ThirdPlacesResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/euro")
public class EuroController {

    private final EuroService euroService;
    private final EuroMatchService euroMatchService;


    @GetMapping("/calendar")
    @CrossOrigin
    public EuroCalendarResponse getEuroCalendar(
            @RequestParam(required = false) String stage
    ) {
        return euroService.getEuroCalendar( stage );
    }

    @GetMapping("/tables")
    @CrossOrigin
    public GroupStageTables getEuroGroupData(
            @RequestParam(required = false) String group
    ) {
        return euroService.getGroupsTables( group );
    }

    @GetMapping("/next-match")
    @CrossOrigin
    public EuroMatch getNextMatch() {
        return euroMatchService.getNextEuroMatch();
    }

    @CrossOrigin
    @GetMapping("/third-places")
    ThirdPlacesResponse getThirdPlacesTable() {
        return euroService.getThirdPlacesTable();
    }

    @GetMapping("/group-matches")
    @CrossOrigin
    public EuroMatchSchedule getPlayedMatches(
            @RequestParam(required = false) String stage,
            @RequestParam(required = false) Integer limit
    ) {
        if (limit == null) {
            limit = 0;
        }
        return euroMatchService.getPlayedMatches( stage, limit );
    }

    @GetMapping("/current-stage")
    @CrossOrigin
    public CurrentStage getCurrentStage() {
        return euroService.getCurrentStage();
    }
}

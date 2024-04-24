package pl.spoda.ks.api.euro;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.match.EuroMatchService;
import pl.spoda.ks.euro.EuroService;
import pl.spoda.ks.euro.model.response.EuroCalendarResponse;
import pl.spoda.ks.euro.model.response.GroupStageTables;

@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/euro")
public class EuroController {

    private final EuroService euroService;
    private final EuroMatchService euroMatchService;


    @GetMapping("/calendar/{group}")
    public EuroCalendarResponse getEuroCalendar(
            @PathVariable(required = false) String group
    ) {
        return euroService.getEuroCalendar( group );
    }

    @GetMapping("/tables/{group}")
    public GroupStageTables getEuroGroupData(
            @PathVariable(required = false) String group
    ) {
        return euroService.getGroupsTables( group );
    }

    @GetMapping("/next-matches/")
    public EuroMatchSchedule getNextMatches() {
        return euroMatchService.getNotPlayedMatches();
    }

    @GetMapping("/group-matches/{group}")
    public EuroMatchSchedule getPlayedMatches(
            @PathVariable(required = false) String group,
            @RequestParam(required = false) Integer limit
    ) {
        return euroMatchService.getPlayedMatches( group, limit );
    }
}

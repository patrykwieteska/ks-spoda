package pl.spoda.ks.euro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.euro.model.Player;
import pl.spoda.ks.euro.model.TournamentGroup;
import pl.spoda.ks.euro.model.TournamentStage;
import pl.spoda.ks.euro.model.request.MatchRequest;
import pl.spoda.ks.euro.model.response.EuroCalendarResponse;
import pl.spoda.ks.euro.model.response.GroupStageTables;
import pl.spoda.ks.euro.model.response.MatchSquadResponse;
import pl.spoda.ks.euro.model.response.ThirdPlacesResponse;

@FeignClient(url = "${euro.service.url}", value = "EuroClient")
public interface EuroClient {


    @GetMapping("/calendar")
    EuroCalendarResponse getEuroCalendar(@RequestParam(required = false) TournamentGroup group);

    @GetMapping("/groups-tables")
    GroupStageTables getGroupsTables(@RequestParam(required = false) TournamentGroup group);

    @PostMapping("/add-result")
    void addResult(@RequestBody MatchRequest request);

    @GetMapping("/current-stage")
    TournamentStage getCurrentStage();

    @PutMapping("/clear-result/{matchId}")
    void clearResult(@PathVariable Integer matchId);

    @PostMapping("/player")
    void updateEuroPlayer(@RequestBody Player build);

    @GetMapping("/teams/{matchNumber}")
    MatchSquadResponse getMatchTeams(@PathVariable Integer matchNumber);

    @GetMapping("/third-places")
    ThirdPlacesResponse getThirdPlacesTable();

}

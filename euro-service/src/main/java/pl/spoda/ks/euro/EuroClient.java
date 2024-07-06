package pl.spoda.ks.euro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.euro.model.CurrentStage;
import pl.spoda.ks.euro.model.Player;
import pl.spoda.ks.euro.model.TournamentGroup;
import pl.spoda.ks.euro.model.request.EuroMatchRequest;
import pl.spoda.ks.euro.model.response.EuroCalendarResponse;
import pl.spoda.ks.euro.model.response.GroupStageTables;
import pl.spoda.ks.euro.model.response.MatchSquadResponse;
import pl.spoda.ks.euro.model.response.ThirdPlacesResponse;

@FeignClient(url = "${euro.service.url}", value = "EuroClient")
public interface EuroClient {


    @GetMapping("/calendar/{tournamentId}")
    EuroCalendarResponse getEuroCalendar(
            @PathVariable("tournamentId") String tournamentId,
            @RequestParam(required = false) TournamentGroup group
    );

    @GetMapping("/groups-tables/{tournamentId}")
    GroupStageTables getGroupsTables(
            @PathVariable("tournamentId") String tournamentId,
            @RequestParam(required = false) TournamentGroup group
    );

    @PostMapping("/add-result/{tournamentId}")
    void addResult(
            @PathVariable("tournamentId") String tournamentId,
            @RequestBody EuroMatchRequest request
    );

    @GetMapping("/current-stage/{tournamentId}")
    CurrentStage getCurrentStage(
            @PathVariable("tournamentId") String tournamentId
    );

    @PutMapping("/clear-result/{tournamentId}")
    void clearResult(
            @PathVariable("tournamentId") String tournamentId,
            @RequestParam Integer matchNumber
    );

    @PostMapping("/player")
    void updateEuroPlayer(@RequestBody Player build);

    @GetMapping("/teams/{tournamentId}")
    MatchSquadResponse getMatchTeams(
            @PathVariable("tournamentId") String tournamentId,
            @RequestParam Integer matchNumber
    );

    @GetMapping("/third-places/{tournamentId}")
    ThirdPlacesResponse getThirdPlacesTable(
            @PathVariable("tournamentId") String tournamentId
    );

    @PostMapping("/new")
    String addEuroTournament();

}

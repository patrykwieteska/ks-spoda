package pl.spoda.ks.euro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.spoda.ks.euro.model.TournamentGroup;
import pl.spoda.ks.euro.model.TournamentStage;
import pl.spoda.ks.euro.model.request.MatchRequest;
import pl.spoda.ks.euro.model.response.EuroCalendarResponse;
import pl.spoda.ks.euro.model.response.GroupStageTables;

@FeignClient(url = "${euro.service.url}", value = "EuroClient")
public interface EuroClient {


    @GetMapping("/calendar/{group}")
    EuroCalendarResponse getEuroCalendar(@PathVariable(required = false) TournamentGroup group);

    @GetMapping("/groups-tables/{group}")
    GroupStageTables getGroupsTables(@PathVariable(required = false) TournamentGroup group);

    @PostMapping("/add-result")
    EuroCalendarResponse addResult(@RequestBody MatchRequest request);

    @GetMapping("/current-stage")
    TournamentStage getCurrentStage();

}

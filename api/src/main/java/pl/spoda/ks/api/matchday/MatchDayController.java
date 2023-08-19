package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.api.matchday.model.MatchDayStored;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequestMapping("${base.request}/match-days")
@RequiredArgsConstructor
public class MatchDayController {

    private final MatchDayService matchDayService;

    @PostMapping
    @LogEvent
    public ResponseEntity<BaseResponse> createMatchDay(
            @RequestBody CreateMatchDayRequest request
            ) {
        return matchDayService.createMatchDay(request);
    }


    @PutMapping("/complete")
    @LogEvent
    public ResponseEntity<BaseResponse> completeLeague(
            @RequestBody MatchDayStored request
            ) {
        return matchDayService.completeMatchDay(request);
    }

    @GetMapping("/{id}")
    @LogEvent
    public ResponseEntity<BaseResponse> initMatchDay(
            @PathVariable(name="id") Integer matchDayId
    ) {
        return matchDayService.initMatchDay(matchDayId);
    }
}

package pl.spoda.ks.api.round;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.round.model.CreateRoundRequest;
import pl.spoda.ks.comons.aspects.LogEvent;

@RestController
@RequestMapping("${base.request}/rounds")
@RequiredArgsConstructor
public class RoundController {

    private final RoundService roundService;

    @PostMapping
    @LogEvent
    public ResponseEntity<BaseResponse> createRound(
            @RequestBody CreateRoundRequest request
            ) {
        return roundService.createRound(request);
    }
}

package pl.spoda.ks.api.player;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.player.model.PlayerRequest;
import pl.spoda.ks.comons.aspects.LogEvent;


@RestController
@RequiredArgsConstructor
@RequestMapping("${base.request}/players")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping("/create")
    @LogEvent
    @CrossOrigin
    public ResponseEntity<BaseResponse> addPlayer(
            @Valid @RequestBody PlayerRequest playerRequest
    ) {
        return playerService.addPlayer(playerRequest);
    }

    @CrossOrigin
    @GetMapping
    @LogEvent
    public ResponseEntity<BaseResponse> getPlayers(
    ) {
        return playerService.getPlayers();
    }

    @GetMapping("/{playerId}")
    @LogEvent
    public ResponseEntity<BaseResponse> getPlayer(
            @PathVariable(name = "playerId") Integer playerId
    ) {
        return playerService.getPlayer(playerId);
    }

    @PostMapping("/edit/{playerId}")
    @LogEvent
    public ResponseEntity<BaseResponse> editPlayer(
            @PathVariable Integer playerId,
            @RequestBody PlayerRequest playerRequest
    ) {
        return playerService.editPlayer(playerId, playerRequest);
    }

    @DeleteMapping("/delete/{playerId}")
    @LogEvent
    public ResponseEntity<BaseResponse> editPlayer(
            @PathVariable(name="playerId") Integer playerId
    ) {
        return playerService.deletePlayer(playerId);
    }

    @GetMapping("/league")
    @LogEvent
    @CrossOrigin
    public ResponseEntity<BaseResponse> getLeaguePlayersBySeason(
            @RequestParam(name="seasonId") Integer seasonId
    ) {
        return playerService.getLeaguePlayersBySeason( seasonId );
    }
}

package pl.spoda.ks.api.player;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.euro.EuroService;

@Service
@RequiredArgsConstructor
public class EuroPlayerService {

    private final EuroService euroService;


    public void updatePlayerData(
            Integer playerId,
            String alias,
            String playerImg
    ) {
        euroService.updatePlayerData(playerId,alias,playerImg);
    }
}

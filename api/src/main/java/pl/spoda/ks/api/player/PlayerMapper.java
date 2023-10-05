package pl.spoda.ks.api.player;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.api.player.model.PlayerRequest;
import pl.spoda.ks.database.dto.PlayerDto;

import java.util.List;

@Service
public class PlayerMapper {

    public PlayerDto mapToPlayerDto(PlayerRequest request) {
        return PlayerDto.builder()
                .alias( request.getAlias() )
                .name( request.getName() )
                .build();
    }

    public PlayerData mapToPlayerData(PlayerDto playerDto) {
        return PlayerData.builder()
                .alias( playerDto.getAlias() )
                .id( playerDto.getId() )
                .name( playerDto.getName() )
                .build();
    }

    public List<PlayerData> mapToPlayerDataList(List<PlayerDto> playerList) {
       return playerList.stream()
                .map( this::mapToPlayerData )
                .toList();
    }
}

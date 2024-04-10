package pl.spoda.ks.api.player;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.player.model.PlayerData;
import pl.spoda.ks.api.player.model.PlayerRequest;
import pl.spoda.ks.comons.utils.CollectionUtils;
import pl.spoda.ks.database.dto.PlayerDto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerMapper {

    public PlayerDto mapToPlayerDto(PlayerRequest request) {
        return PlayerDto.builder()
                .alias( request.getAlias() )
                .name( request.getName() )
                .playerImg( request.getPlayerImg() )
                .build();
    }

    public Set<PlayerDto> mapToPlayerDtoList(List<PlayerData> playerDataList) {
        return CollectionUtils.emptyIfNull(playerDataList).stream()
                .map( playerData -> PlayerDto.builder()
                        .id( playerData.getId() )
                        .alias( playerData.getAlias() )
                        .name( playerData.getName() )
                        .playerImg( playerData.getPlayerImg() )
                        .desc( playerData.getDesc() )
                        .build() )
                .collect( Collectors.toSet());
    }

    public PlayerData mapToPlayerData(PlayerDto playerDto) {
        return PlayerData.builder()
                .alias( playerDto.getAlias() )
                .id( playerDto.getId() )
                .name( playerDto.getName() )
                .playerImg( playerDto.getPlayerImg() )
                .desc( playerDto.getDesc() )
                .joinDate( playerDto.getJoinDate() )
                .build();
    }

    public Set<PlayerData> mapToPlayerDataList(Collection<PlayerDto> playerList) {
       return playerList.stream()
                .map( this::mapToPlayerData )
                .collect( Collectors.toSet());
    }
}

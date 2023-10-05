package pl.spoda.ks.database.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.exceptions.SpodaDatabaseException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.PlayerRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceDB {

    private final PlayerRepository playerRepository;
    private final BaseServiceDB baseServiceDB;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    @LogEvent
    @Transactional
    public Integer add(PlayerDto playerDto) {
        Optional<Player> existingPlayer = playerRepository.findByAlias( playerDto.getAlias() );
        if (existingPlayer.isPresent()) {
            throw new SpodaDatabaseException( String.format( InfoMessage.ALIAS_ALREADY_EXISTS,
                    playerDto.getAlias() ) );
        }
        Player player = mapper.mapToPlayer( playerDto );
        baseServiceDB.createEntity( player );

        Player newPlayer;
        try {
            newPlayer = playerRepository.save( player );
        } catch (Exception e) {
            log.error( e.getMessage(),e );
            throw new SpodaDatabaseException( "Error during saving player" );
        }
        return newPlayer.getId();
    }

    @LogEvent
    public List<PlayerDto> getPlayerList() {
        return playerRepository.findAll().stream()
                .map( mapper::mapToPlayerDto )
                .toList();
    }

    @LogEvent
    public PlayerDto getPlayer(Integer playerId) {
        Player storedPlayer = readPlayer( playerId );
        return mapper.mapToPlayerDto(storedPlayer);
    }

    @Transactional
    public void editPlayer(Integer playerId, PlayerDto playerDto) {
        Player storedPlayer = readPlayer( playerId );
        baseServiceDB.updateEntity( storedPlayer );
        updateNotNullParams(storedPlayer,playerDto);

        try {
            playerRepository.save( storedPlayer );
        } catch (Exception e) {
            log.error( e.getMessage(),e );
            throw new SpodaDatabaseException( "Error during updating player");
        }
    }

    private Player readPlayer(Integer playerId) {
        Optional<Player> storedPlayer = playerRepository.findById( playerId );

        if(storedPlayer.isEmpty()) {
            throw new SpodaApplicationException( String.format("Player with id=%d does not exist" ,playerId));
        }

        return storedPlayer.get();
    }

    private void updateNotNullParams(Player player, PlayerDto playerDto) {
        if (!Objects.isNull(playerDto.getName())) {
            player.setName( playerDto.getName() );
        }

        if (!Objects.isNull(playerDto.getAlias())) {
            player.setAlias( playerDto.getAlias() );
        }
    }

    @Transactional
    @LogEvent
    public void deletePlayer(Integer playerId) {
        // Dodać walidacje usunięcia, gdy gracz jest przypisany do jakiegokolwiek meczu
        Player storedPlayer = readPlayer( playerId );
        playerRepository.delete( storedPlayer );
    }
}

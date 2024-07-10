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
import pl.spoda.ks.comons.utils.CollectionUtils;
import pl.spoda.ks.database.dto.LeagueTableDto;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.database.dto.TableBaseDto;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.PlayerRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceDB {

    private final PlayerRepository playerRepository;
    private final BaseServiceDB baseServiceDB;
    private final LeagueTableServiceDB leagueTableServiceDB;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    @LogEvent
    @Transactional
    public Integer add(
            PlayerDto playerDto,
            Integer leagueId
    ) {
        Optional<Player> existingPlayer = playerRepository.findByAlias( playerDto.getAlias() );
        if (playerDto.getId() == null && existingPlayer.isPresent()) {
            throw new SpodaApplicationException( String.format( InfoMessage.ALIAS_ALREADY_EXISTS ));
        }
        Player newPlayer = savePlayer( playerDto );
        leagueTableServiceDB.addPlayerToLeague(newPlayer,leagueId);
        return newPlayer.getId();
    }

    private Player savePlayer(PlayerDto playerDto) {
        Player player = mapper.mapToPlayer( playerDto );
        baseServiceDB.createEntity( player );

        Player newPlayer;
        try {
            newPlayer = playerRepository.save( player );
        } catch (Exception e) {
            log.error( e.getMessage(),e );
            throw new SpodaDatabaseException( "Error during saving player" );
        }
        return newPlayer;
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
        PlayerDto playerDto = mapper.mapToPlayerDto( storedPlayer );
        playerDto.setJoinDate( storedPlayer.getCreationDate().toLocalDate() );
        return playerDto;
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

        player.setPlayerImg( playerDto.getPlayerImg() );
        player.setDesc( playerDto.getDesc() );
    }

    @Transactional
    @LogEvent
    public void deletePlayer(Integer playerId) {
        // Dodać walidacje usunięcia, gdy gracz jest przypisany do jakiegokolwiek meczu
        Player storedPlayer = readPlayer( playerId );
        playerRepository.delete( storedPlayer );
    }
    @Transactional
    public Set<Player> savePlayerList(Set<PlayerDto> players) {
        Set<Player> newPlayers = prepareNewPlayerSet( players );
        Set<Player> existingPlayers = prepareExistingPlayers(players);
        newPlayers.addAll( existingPlayers );
        return newPlayers;
    }

    private Set<Player> prepareNewPlayerSet(Set<PlayerDto> players) {
        return players.stream()
                .filter( player -> player.getId() == null )
                .map( this::savePlayer )
                .collect( Collectors.toSet());
    }

    private Set<Player> prepareExistingPlayers(Set<PlayerDto> players) {
        return players.stream()
                .filter( player -> player.getId()!= null )
                .map( player ->readPlayer(player.getId()))
                .collect( Collectors.toSet());
    }

    public List<PlayerDto> getPlayerListByLeagueId(Integer leagueId) {
        List<LeagueTableDto> leaguePlayers = leagueTableServiceDB.getLeagueTable( leagueId );
        return CollectionUtils.emptyIfNull( leaguePlayers ).stream()
                .map( TableBaseDto::getPlayer )
                .toList();
    }
}

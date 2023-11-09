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
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.entity.LeaguesPlayers;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.repository.LeagueRepository;
import pl.spoda.ks.database.repository.LeaguesPlayersRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeagueServiceDB {

    private final LeagueRepository leagueRepository;
    private final PlayerServiceDB playerServiceDB;
    private final BaseServiceDB baseServiceDB;
    private final LeaguesPlayersRepository leaguesPlayersRepository;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    @LogEvent
    @Transactional
    public LeagueDto save(LeagueDto leagueDto) {
        Set<Player> players = playerServiceDB.savePlayerList(leagueDto.getPlayerList());
        League league = mapper.mapToLeagueEntity( leagueDto );
        league.setPlayers( players );
        baseServiceDB.createEntity( league );
        League storedLeague;
        try {
            storedLeague = leagueRepository.save( league );
        } catch (Exception e) {
            throw new SpodaDatabaseException( "Error during saving a league" );
        }
        saveLeaguePlayersRelations( players, storedLeague.getId() );
        return mapper.mapToLeagueDto( storedLeague );
    }

    private void saveLeaguePlayersRelations(Set<Player> players, Integer leagueId) {
        leaguesPlayersRepository.saveAll( players.stream()
                .map( player -> mapToLeaguePlayersEntity( leagueId, player ) )
                .toList());
    }

    private LeaguesPlayers mapToLeaguePlayersEntity( Integer leagueId, Player player) {
        LeaguesPlayers leaguesPlayersEntity = LeaguesPlayers.builder()
                .playerId( player.getId() )
                .leagueId( leagueId )
                .build();
        baseServiceDB.createEntity( leaguesPlayersEntity );
        return leaguesPlayersEntity;
    }

    public boolean isLeagueAlreadyExists(String name) {
        return leagueRepository.findByName( name.toUpperCase() ).isPresent();
    }

    public List<LeagueDto> getLeagues() {
        return leagueRepository.findAll().stream()
                .map( league -> {
                    LeagueDto leagueDto = mapper.mapToLeagueDto( league );
                    leagueDto.setCreationDate( league.getCreationDate() );
                    return leagueDto;
                } )
                .toList();
    }

    public LeagueDto getSingleLeague(Integer leagueId) {
        League storedLeague = leagueRepository.findById( leagueId ).orElse( null );
        checkIfLeagueExists( leagueId, storedLeague );
        return mapper.mapToLeagueDto( storedLeague );
    }

    private void checkIfLeagueExists(Integer leagueId, League storedLeague) {
        if(storedLeague == null)
            throw new SpodaApplicationException( InfoMessage.getMessage( InfoMessage.LEAGUE_NOT_FOUND, leagueId.toString() ) );
    }
}

package pl.spoda.ks.database.repository;

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
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.entity.League;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeagueServiceDb {

    private final LeagueRepository leagueRepository;
    private final DbService dbService;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    @LogEvent
    @Transactional
    public LeagueDto save(LeagueDto leagueDto) {
        League league = mapper.mapToLeagueEntity( leagueDto );
        dbService.createEntity( league );
        League storedLeague;

        try {
            storedLeague = leagueRepository.save( league );
        } catch (Exception e) {
            throw new SpodaDatabaseException( "Error during saving a league" );
        }
        return mapper.mapToLeagueDto( storedLeague );
    }

    public boolean isLeagueAlreadyExists(String name) {
        return leagueRepository.findByName( name ).isPresent();
    }

    public List<LeagueDto> getLeagues() {
        return leagueRepository.findAll().stream()
                .map( mapper::mapToLeagueDto )
                .toList();
    }

    public LeagueDto getSingleLeague(Integer leagueId) {
        Optional<League> storedLeague = leagueRepository.findById( leagueId );
        if(storedLeague.isEmpty())
            throw new SpodaApplicationException( InfoMessage.getMessage( InfoMessage.LEAGUE_NOT_FOUND,leagueId.toString() ) );
        return mapper.mapToLeagueDto( storedLeague.get() );
    }
}

package pl.spoda.ks.database.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.dto.RoundDto;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.Round;
import pl.spoda.ks.database.mapper.EntityMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundServiceDb {

    private final DbService dbService;
    private final LeagueRepository leagueRepository;
    private final RoundRepository roundRepository;
    private final EntityMapper entityMapper = Mappers.getMapper( EntityMapper.class );

    @Transactional
    @LogEvent
    public RoundDto createRound(RoundDto roundDto) {
        League storedLeague = leagueRepository.findById( roundDto.getLeagueId()).orElse( null );
        validateLeague( roundDto, storedLeague );

        Round round = entityMapper.mapToRound( roundDto );
        round.setLeague( storedLeague);
        dbService.createEntity( round );

        Round save = roundRepository.save( round );
        return entityMapper.mapToRoundDto( save );
    }

    private void validateLeague(RoundDto roundDto, League storedLeague) {
        if (storedLeague ==null)
            throw new SpodaApplicationException( String.format( "League with id: %d is not exist", roundDto.getLeagueId() ) );

        if (BooleanUtils.isTrue( storedLeague.getIsFinished()))
            throw new SpodaApplicationException( String.format( "League %d is finished. Could not edit it",
                    roundDto.getLeagueId() ) );

        if(roundRepository.findByDate( roundDto.getDate() ).isPresent()) {
            throw new SpodaApplicationException( String.format( "Round with date: %s already exists",
                    roundDto.getDate() ) );
        }
    }

    public List<RoundDto> getRoundsByLeagueId(Integer leagueId) {
        List<Round> storedRound = roundRepository.findByLeagueId( leagueId );
        return storedRound.stream()
                .map( entityMapper::mapToRoundDto )
                .toList();
    }
}

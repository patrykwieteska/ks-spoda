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

import java.util.Optional;

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
        Optional<League> storedLeague = leagueRepository.findById( roundDto.getLeagueId() );

        if (storedLeague.isEmpty())
            throw new SpodaApplicationException( String.format( "League with id: %d is not exist", roundDto.getLeagueId() ) );

        if (BooleanUtils.isTrue(storedLeague.get().getIsFinished()))
            throw new SpodaApplicationException( String.format( "League %d is finished. Could not edit it",
                    roundDto.getLeagueId() ) );

        Round round = entityMapper.mapToRound( roundDto );
        dbService.createEntity( round );

        return entityMapper.mapToRoundDto( roundRepository.save( round ) );
    }
}

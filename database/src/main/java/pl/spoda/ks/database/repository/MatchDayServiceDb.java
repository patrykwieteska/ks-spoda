package pl.spoda.ks.database.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.MatchDay;
import pl.spoda.ks.database.mapper.EntityMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MatchDayServiceDb {

    private final DbService dbService;
    private final LeagueRepository leagueRepository;
    private final MatchDayRepository matchDayRepository;
    private final EntityMapper entityMapper = Mappers.getMapper( EntityMapper.class );

    @Transactional
    @LogEvent
    public MatchDayDto createMatchDay(MatchDayDto matchDayDto) {
        League storedLeague = leagueRepository.findById( matchDayDto.getLeagueId() ).orElse( null );
        validateLeague( matchDayDto, storedLeague );
        validateMatchDay( matchDayDto, storedLeague.getId() );

        MatchDay matchDay = entityMapper.mapToMatchDay( matchDayDto );
        matchDay.isFinished( false );
        matchDay.league( storedLeague );
        dbService.createEntity( matchDay );

        MatchDay save = matchDayRepository.save( matchDay );
        return entityMapper.mapToMatchDayDto( save );
    }

    @LogEvent
    public List<MatchDayDto> getMatchDaysByLeagueId(Integer leagueId) {
        List<MatchDay> storedMatchDay = matchDayRepository.findByLeagueId( leagueId );
        List<MatchDayDto> matchDayDtoStream = storedMatchDay.stream()
                .map( entityMapper::mapToMatchDayDto )
                .sorted( Comparator.comparing( MatchDayDto::getDate ) )
                .toList();

        IntStream.range( 0, matchDayDtoStream.size() )
                .forEach( matchDayNumber -> matchDayDtoStream.get( matchDayNumber ).setLeagueMatchDayNumber( matchDayNumber + 1 ) );

        return matchDayDtoStream.stream()
                .sorted( Comparator.comparing( MatchDayDto::getLeagueMatchDayNumber ).reversed() )
                .toList();
    }

    @LogEvent
    public MatchDayDto getMatchDay(Integer matchDayId) {
        MatchDay storedMathDay = matchDayRepository.findById( matchDayId ).orElse( null );
        if(storedMathDay == null)
            throw new SpodaApplicationException( String.format( "There is no match day with id: %d",matchDayId ) );

        return entityMapper.mapToMatchDayDto( storedMathDay );
    }

    private void validateMatchDay(MatchDayDto matchDayDto, Integer leagueId) {
        if (matchDayRepository.findByDate( matchDayDto.getDate() ).isPresent()) {
            throw new SpodaApplicationException( String.format( "MatchDay with date: %s already exists",
                    matchDayDto.getDate() ) );
        }

        List<MatchDay> storedMatchDay = matchDayRepository.findByLeagueId( leagueId );
        if (isAnyMatchDayUnfinished( storedMatchDay ))
            throw new SpodaApplicationException( String.format( "There are some matchDays unfinished in league: %d",
                    leagueId ) );
    }


    private void validateLeague(MatchDayDto matchDayDto, League storedLeague) {
        if (storedLeague == null)
            throw new SpodaApplicationException( String.format( "League with id: %d is not exist", matchDayDto.getLeagueId() ) );

        if (BooleanUtils.isTrue( storedLeague.getIsFinished() ))
            throw new SpodaApplicationException( String.format( "League %d is finished. Could not edit it",
                    matchDayDto.getLeagueId() ) );
    }

    private boolean isAnyMatchDayUnfinished(List<MatchDay> storedMatchDay) {
        return storedMatchDay.stream()
                .anyMatch( matchDay -> BooleanUtils.isNotTrue( matchDay.getIsFinished() ) );
    }

    public boolean isAnyMatchDayUnfinished(Integer leagueId) {
        return matchDayRepository.findByLeagueId( leagueId ).stream()
                .anyMatch( matchDay -> BooleanUtils.isNotTrue( matchDay.getIsFinished() ) );
    }

    public void completeMatchDay(Integer matchDayId) {
      /*
        TODO gameMatchServiceDb - sprawdzenie czy istnieje jakikolwiek niezakończony mecz w danej kolejce.
           if(gameMatchServiceDb.isAnyMatchDayUnfinished(matchDayId))
           throw new SpodaApplicationException( InfoMessage.ROUNDS_NOT_FINISHED );
      */

        MatchDay storedMatchDay = matchDayRepository.findById( matchDayId ).orElse( null );
        checkIfMatchDayExists( matchDayId, storedMatchDay );
        storedMatchDay.isFinished( true );
        dbService.updateEntity( storedMatchDay );
        matchDayRepository.save( storedMatchDay );
    }

    private void checkIfMatchDayExists(Integer matchDayId, MatchDay storedMatchDay) {
        if (storedMatchDay == null)
            throw new SpodaApplicationException( InfoMessage.getMessage( InfoMessage.LEAGUE_NOT_FOUND, matchDayId.toString() ) );
    }


}

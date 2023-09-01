package pl.spoda.ks.database.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.aspects.LogEvent;
import pl.spoda.ks.comons.date.DateService;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.exceptions.SpodaDatabaseException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.Season;
import pl.spoda.ks.database.mapper.EntityMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeasonServiceDb {

    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;
    private final MatchDayServiceDb matchDayServiceDb;
    private final DbService dbService;
    private final DateService dateService;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    @LogEvent
    @Transactional
    public SeasonDto save(SeasonDto seasonDto) {
        League storedLeague = leagueRepository.findById( seasonDto.getLeagueId() ).orElse( null );
        validateLeague( seasonDto.getLeagueId(), storedLeague );

        validateUnfinishedSeasonsInTheLeague( storedLeague.getId() );

        Season season = mapper.mapToSeason( seasonDto );
        season.league( storedLeague );
        dbService.createEntity( season );

        try {
            seasonRepository.save( season );
        } catch (Exception e) {
            throw new SpodaDatabaseException( "Error during saving a league" );
        }
        return mapper.mapToSeasonDto( season );
    }

    private void validateUnfinishedSeasonsInTheLeague(Integer leagueId) {
        if (seasonRepository.findByLeagueId( leagueId ).stream()
                .anyMatch( season -> BooleanUtils.isNotTrue( season.getIsFinished() ) ))
            throw new SpodaApplicationException(InfoMessage.getMessage( InfoMessage.UNFINISHED_SEASONS_IN_LEAGUE,
                    leagueId.toString() ));
    }

    public List<SeasonDto> getSeasonsByLeague(Integer leagueId) {
        return seasonRepository.findByLeagueId(leagueId).stream()
                .map( mapper::mapToSeasonDto )
                .toList();
    }

    public SeasonDto getSingleSeason(Integer seasonId) {
        Season storedSeason = seasonRepository.findById( seasonId ).orElse( null );
        checkIfSeasonExists( seasonId, storedSeason );
        return mapper.mapToSeasonDto( storedSeason );
    }

    private void checkIfSeasonExists(Integer seasonId, Season storedSeason) {
        if (storedSeason == null)
            throw new SpodaApplicationException( InfoMessage.getMessage( InfoMessage.SEASON_NOT_FOUND, seasonId.toString() ) );
    }

    @Transactional
    public void completeSeason(Integer seasonId) {
        if (matchDayServiceDb.isAnyMatchDayUnfinished( seasonId ))
            throw new SpodaApplicationException( InfoMessage.MATCHDAYS_NOT_FINISHED );

        Season storedSeason = seasonRepository.findById( seasonId ).orElse( null );
        checkIfSeasonExists( seasonId, storedSeason );
        storedSeason
                .isFinished( true )
                .endDate( dateService.getCurrentDate());
        dbService.updateEntity( storedSeason );
        seasonRepository.save( storedSeason );
    }

    private void validateLeague(Integer leagueId, League storedLeague) {
        if (storedLeague == null)
            throw new SpodaApplicationException( String.format( "League with id: %d is not exist", leagueId ) );
    }
}

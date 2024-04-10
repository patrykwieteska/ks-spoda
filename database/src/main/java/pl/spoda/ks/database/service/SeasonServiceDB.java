package pl.spoda.ks.database.service;

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
import pl.spoda.ks.database.repository.LeagueRepository;
import pl.spoda.ks.database.repository.SeasonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeasonServiceDB {

    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;
    private final MatchDayServiceDB matchDayServiceDb;
    private final BaseServiceDB baseServiceDB;
    private final DateService dateService;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    @LogEvent
    @Transactional
    public SeasonDto save(SeasonDto seasonDto) {
        League storedLeague = leagueRepository.findById( seasonDto.getLeagueId() ).orElse( null );
        validateLeague( seasonDto.getLeagueId(), storedLeague );

        int storedSeasonCount = validateUnfinishedSeasonsInTheLeague( storedLeague.getId() );

        Season season = mapper.mapToSeason( seasonDto );
        season.setLeagueSeasonCount( storedSeasonCount + 1 );
        season.league( storedLeague );
        baseServiceDB.createEntity( season );

        try {
            seasonRepository.save( season );
        } catch (Exception e) {
            throw new SpodaDatabaseException( "Error during saving a league" );
        }
        return mapper.mapToSeasonDto( season );
    }

    private int validateUnfinishedSeasonsInTheLeague(Integer leagueId) {
        List<Season> seasons = seasonRepository.findByLeagueId( leagueId );
        if (seasons.stream()
                .anyMatch( season -> BooleanUtils.isNotTrue( season.getIsFinished() ) ))
            throw new SpodaApplicationException( InfoMessage.getMessage( InfoMessage.UNFINISHED_SEASONS_IN_LEAGUE,
                    leagueId.toString() ) );

        return seasons.size();
    }

    public List<SeasonDto> getSeasonsByLeague(Integer leagueId) {
        return seasonRepository.findByLeagueId( leagueId ).stream()
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
                .endDate( dateService.getCurrentDate() );
        baseServiceDB.updateEntity( storedSeason );
        seasonRepository.save( storedSeason );
    }

    private void validateLeague(Integer leagueId, League storedLeague) {
        if (storedLeague == null)
            throw new SpodaApplicationException( String.format( "League with id: %d is not exist", leagueId ) );
    }

    public Integer findLeagueForSeason(Integer seasonId) {
        return seasonRepository.findById( seasonId ).map( Season::getLeagueId ).orElseThrow(
                () -> new SpodaApplicationException( "LeagueId for season: " + seasonId + " not found." )
        );
    }


    public boolean hasNoActiveMatchDay(Integer seasonId) {
        return seasonRepository.countActiveMatchDays( seasonId ).equals( 0 );
    }
}

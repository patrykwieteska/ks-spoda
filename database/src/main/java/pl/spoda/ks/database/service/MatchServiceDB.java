package pl.spoda.ks.database.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.messages.InfoMessage;
import pl.spoda.ks.database.dto.*;
import pl.spoda.ks.database.entity.Match;
import pl.spoda.ks.database.entity.MatchDay;
import pl.spoda.ks.database.entity.MatchTeam;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.MatchDayRepository;
import pl.spoda.ks.database.repository.MatchDetailsRepository;
import pl.spoda.ks.database.repository.MatchRepository;
import pl.spoda.ks.database.repository.SeasonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceDB {

    private final MatchRepository matchRepository;
    private final SeasonRepository seasonRepository;
    private final MatchDayRepository matchDayRepository;
    private final SeasonTableServiceDB seasonTableServiceDB;
    private final MatchDayTableServiceDB matchDayTableServiceDB;
    private final LeagueTableServiceDB leagueTableServiceDB;
    private final BaseServiceDB baseServiceDB;
    private final MatchDetailsServiceDB matchDetailsServiceDB;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );
    private final MatchDetailsRepository matchDetailsRepository;

    public List<MatchDto> findMatchesByLeague(Integer leagueId) {
        List<Integer> seasonIds = seasonRepository.findSeasonIdsByLeagueId( leagueId );
        List<Integer> matchDayIds = matchDayRepository.findMatchDayIdsBySeasonIds( seasonIds );
        List<Match> leagueMatches = matchRepository.findMatchesByMatchDayIds( matchDayIds );
        return leagueMatches.stream()
                .map( mapper::mapToMatchDto )
                .toList();

    }

    @Transactional
    public Integer saveMatch(
            MatchDto matchDto,
            List<LeagueTableDto> leagueTable,
            List<SeasonTableDto> seasonTable,
            List<MatchDayTableDto> matchDayTable,
            List<MatchDetailsDto> matchDetailsDtoList
    ) {
        seasonTableServiceDB.saveTable( seasonTable );
        matchDayTableServiceDB.saveTable( matchDayTable );
        leagueTableServiceDB.saveTable( leagueTable );
        Match matchEntity = mapper.mapToMatch( matchDto );
        MatchDay matchDay =
                matchDayRepository
                        .findById( matchDto.getMatchDayId() )
                        .orElseThrow(
                                () -> new SpodaApplicationException( "There is no match day with id " + matchDto.getMatchDayId()
                                ) );
        MatchTeam homeTeam = mapper.mapToMatchTeam( matchDto.getHomeTeam() );
        MatchTeam awayTeam = mapper.mapToMatchTeam( matchDto.getAwayTeam() );
        baseServiceDB.createEntity( homeTeam );
        baseServiceDB.createEntity( awayTeam );
        matchEntity.setHomeTeam( homeTeam );
        matchEntity.setAwayTeam( awayTeam );

        matchEntity.setMatchDay( matchDay );
        baseServiceDB.createEntity( matchEntity );

        Match save = matchRepository.save( matchEntity );
        matchDetailsServiceDB.saveDetails( matchDetailsDtoList, save.getId() );
        return save.getId();
    }

    public List<MatchDto> findMatchesBySeason(Integer seasonId) {
        List<Integer> matchDayIds = matchDayRepository.findMatchDayIdsBySeasonIds( List.of( seasonId ) );
        List<Match> leagueMatches = matchRepository.findMatchesByMatchDayIds( matchDayIds );
        return leagueMatches.stream()
                .map( mapper::mapToMatchDto )
                .toList();
    }

    public MatchDto getMatchById(Integer matchId) {
        return matchRepository.findById( matchId )
                .map( mapper::mapToMatchDto )
                .orElseThrow( () -> new SpodaApplicationException( "Nie istnieje mecz o podanym id: " + matchId ) );
    }

    public void validateUnfinishedMatchDayMatches(Integer matchDayId) {
        List<Integer> unfinishedMatchesByMatchDay = matchRepository.findUnfinishedMatchesByMatchDay( matchDayId );
        if (!unfinishedMatchesByMatchDay.isEmpty()) {
            throw new SpodaApplicationException( InfoMessage.NOT_FINISHED_MATCHES );
        }
    }

    public Integer getNewestMatch(Integer leagueId) {
        return matchRepository.getNewestMatch( leagueId ).orElse( 0 );
    }

    @Transactional
    public void removeMatch(
            Integer matchId,
            List<MatchDetailsDto> storedMatchDetails,
            List<MatchDayTableDto> matchDayTable,
            List<SeasonTableDto> seasonTable,
            List<LeagueTableDto> leagueTable
    ) {
        matchDetailsRepository.deleteAllById( storedMatchDetails.stream().map( MatchDetailsDto::getId ).toList() );
        matchRepository.deleteById( matchId );

        seasonTableServiceDB.saveTable( seasonTable );
        matchDayTableServiceDB.saveTable( matchDayTable );
        leagueTableServiceDB.saveTable( leagueTable );
    }
}

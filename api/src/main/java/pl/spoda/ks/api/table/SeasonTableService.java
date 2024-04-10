package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.database.dto.SeasonDto;
import pl.spoda.ks.database.dto.SeasonTableDto;
import pl.spoda.ks.database.service.PlayerServiceDB;
import pl.spoda.ks.database.service.SeasonTableServiceDB;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SeasonTableService {


    private final SeasonTableServiceDB seasonTableServiceDB;
    private final PlayerServiceDB playerServiceDB;
    private final SortTableService sortTableService;
    private final UpdateTableService updateTableService;

    public List<SeasonTableDto> getCurrentTable(
            Integer seasonId,
            List<MatchDetailsDto> matchDetailsList,
            PointCountingMethod pointCountingMethod
    ) {
        Set<SeasonTableDto> currentSeasonTable = seasonTableServiceDB.getCurrentSeasonTable( seasonId );
        List<SeasonTableDto> updatedMatchTable = new ArrayList<>( currentSeasonTable );

        matchDetailsList.forEach( matchDetail -> {
            if (isPlayerInTable( currentSeasonTable, matchDetail.getPlayerId() )) {
                updateTableRow( matchDetail, updatedMatchTable );
            } else {
                updatedMatchTable.add( getNewRow( matchDetail,currentSeasonTable.size(),pointCountingMethod ) );
            }
        } );
        return sortTableService.getSortedTable( updatedMatchTable, pointCountingMethod,true );
    }

    private void updateTableRow(MatchDetailsDto matchDetail, List<SeasonTableDto> updatedMatchTable) {
        updatedMatchTable.stream()
                .filter( tableRow -> tableRow.getPlayer().getId().equals( matchDetail.getPlayerId() ) )
                .findFirst()
                .ifPresent( row -> {
                    row.setCurrentRating( matchDetail.getSeasonRatingAfterMatch() );
                    row.setMatches( row.getMatches().add( BigDecimal.ONE ) );
                    row.setPointsTotal( row.getPointsTotal().add( new BigDecimal( matchDetail.getMatchPoints() ) ) );
                    row.setMatchInProgress( matchDetail.getMatchInProgress() );
                } );
    }

    private SeasonTableDto getNewRow(MatchDetailsDto matchDetail, int tableSize, PointCountingMethod pointCountingMethod) {
        int defaultPosition = tableSize +1;
        return SeasonTableDto.builder()
                .seasonId( matchDetail.getSeasonId() )
                .player( playerServiceDB.getPlayer( matchDetail.getPlayerId() ) )
                .matches( BigDecimal.ONE )
                .pointsTotal( new BigDecimal( matchDetail.getMatchPoints() ) )
                .currentPosition( defaultPosition)
                .previousPosition( defaultPosition )
                .standbyPosition( defaultPosition )
                .currentRating( preparePointCountingMethod( pointCountingMethod, matchDetail.getSeasonRatingAfterMatch()) )
                .isNewPlayer( true )
                .matchInProgress( matchDetail.getMatchInProgress() )
                .build();
    }

    private boolean isPlayerInTable(Set<SeasonTableDto> currentSeasonTable, Integer playerId) {
        return currentSeasonTable.stream()
                .anyMatch( row -> row.getPlayer().getId().equals( playerId ) );
    }

    private BigDecimal preparePointCountingMethod(
            PointCountingMethod pointCountingMethod,
            BigDecimal ratingAfterMatch
    ) {
        return pointCountingMethod.equals(  PointCountingMethod.RATING)
                ? ratingAfterMatch
                : null;
    }

    public List<SeasonTableDto> updateTable(
            List<MatchDetailsDto> matchDetailsList,
            List<MatchDetailsDto> updatedMatchDetailsList,
            MatchDayDto matchDayDto
    ) {
        SeasonDto season = matchDayDto.getSeason();
        List<SeasonTableDto> seasonTable = seasonTableServiceDB.getSeasonTable( season.getId() );
        List<SeasonTableDto> updatedSeasonTable = new ArrayList<>( seasonTable );
        PointCountingMethod countingMethod = PointCountingMethod.getByName( season.getPointCountingMethod() );

        updateTableService.updateTable( updatedSeasonTable,matchDetailsList,updatedMatchDetailsList,countingMethod );
        return sortTableService.getSortedTable( updatedSeasonTable, countingMethod,false );
    }

    public List<SeasonTableDto> updateBeforeRemoveMatch(List<MatchDetailsDto> matchDetailsList, Integer seasonId) {
        List<SeasonTableDto> seasonTable = seasonTableServiceDB.getSeasonTable( seasonId );
        List<SeasonTableDto> updatedSeasonTable = new ArrayList<>( seasonTable );
        updateTableService.updateBeforeRemoveMatch( updatedSeasonTable, matchDetailsList );
        return updatedSeasonTable;
    }
}

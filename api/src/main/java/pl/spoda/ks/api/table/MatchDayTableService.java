package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.database.dto.MatchDayTableDto;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.service.MatchDayTableServiceDB;
import pl.spoda.ks.database.service.PlayerServiceDB;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchDayTableService {


    private final MatchDayTableServiceDB matchDayTableServiceDB;
    private final PlayerServiceDB playerServiceDB;
    private final SortTableService sortTableService;
    private final UpdateTableService updateTableService;

    public List<MatchDayTableDto> getCurrentTable(
            Integer matchDayId,
            List<MatchDetailsDto> matchDetailsList,
            PointCountingMethod pointCountingMethod
    ) {
        Set<MatchDayTableDto> currentMatchDayTable = matchDayTableServiceDB.getCurrentMatchDayTable( matchDayId );
        List<MatchDayTableDto> updatedMatchTable = new ArrayList<>( currentMatchDayTable );

        matchDetailsList.forEach( matchDetail -> {
            if (isPlayerInTable( currentMatchDayTable, matchDetail.getPlayerId() )) {
                updateTableRow( matchDetail, updatedMatchTable );
            } else {
                updatedMatchTable.add( getNewRow( matchDetail, currentMatchDayTable.size(), pointCountingMethod ) );
            }
        } );
        return sortTableService.getSortedTable( updatedMatchTable, pointCountingMethod, true );
    }

    private void updateTableRow(MatchDetailsDto matchDetail, List<MatchDayTableDto> updatedMatchTable) {
        updatedMatchTable.stream()
                .filter( tableRow -> tableRow.getPlayer().getId().equals( matchDetail.getPlayerId() ) )
                .findFirst()
                .ifPresent( row -> {
                    row.setCurrentRating( matchDetail.getMatchDayRatingAfterMatch() );
                    row.setMatches( row.getMatches().add( BigDecimal.ONE ) );
                    row.setPointsTotal( row.getPointsTotal().add( new BigDecimal( matchDetail.getMatchPoints() ) ) );
                } );
    }

    private MatchDayTableDto getNewRow(MatchDetailsDto matchDetail, int tableSize, PointCountingMethod pointCountingMethod) {
        int defaultPosition = tableSize + 1;
        return MatchDayTableDto.builder()
                .matchDayId( matchDetail.getMatchDayId() )
                .player( playerServiceDB.getPlayer( matchDetail.getPlayerId() ) )
                .matches( BigDecimal.ONE )
                .pointsTotal( new BigDecimal( matchDetail.getMatchPoints() ) )
                .currentPosition( defaultPosition )
                .previousPosition( defaultPosition )
                .standbyPosition( defaultPosition )
                .currentRating( preparePointCountingMethod( pointCountingMethod, matchDetail.getMatchDayRatingAfterMatch() ) )
                .build();
    }

    private boolean isPlayerInTable(Set<MatchDayTableDto> currentMatchDayTable, Integer playerId) {
        return currentMatchDayTable.stream()
                .anyMatch( row -> row.getPlayer().getId().equals( playerId ) );
    }

    private BigDecimal preparePointCountingMethod(
            PointCountingMethod pointCountingMethod,
            BigDecimal ratingAfterMatch
    ) {
        return pointCountingMethod.equals( PointCountingMethod.RATING )
                ? ratingAfterMatch
                : null;
    }

    public List<MatchDayTableDto> updateTable(List<MatchDetailsDto> matchDetailsList,
                                              List<MatchDetailsDto> updatedMatchDetailsList, MatchDto matchDto, String pointCountingMethod) {
        List<MatchDayTableDto> matchDayTable = matchDayTableServiceDB.getMatchDayTable( matchDto.getMatchDayId() );
        List<MatchDayTableDto> updatedMatchTable = new ArrayList<>( matchDayTable );
        PointCountingMethod countingMethod = PointCountingMethod.getByName( pointCountingMethod );
        updateTableService.updateTable( updatedMatchTable, matchDetailsList, updatedMatchDetailsList, countingMethod );
        return sortTableService.getSortedTable( updatedMatchTable, countingMethod, false );
    }

    public List<MatchDayTableDto> updateBeforeRemoveMatch(List<MatchDetailsDto> matchDetailsList, Integer matchDayId) {
        List<MatchDayTableDto> matchDayTable = matchDayTableServiceDB.getMatchDayTable( matchDayId );
        List<MatchDayTableDto> updatedMatchTable = new ArrayList<>( matchDayTable );
        updateTableService.updateBeforeRemoveMatch( updatedMatchTable, matchDetailsList );
        return updatedMatchTable;
    }
}

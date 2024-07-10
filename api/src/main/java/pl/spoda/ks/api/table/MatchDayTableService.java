package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
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
    private final UpdateTableService updateTableService;

    public List<MatchDayTableDto> getCurrentTableAfterMatchCreated(
            Integer matchDayId,
            List<MatchDetailsDto> matchDetailsList
    ) {
        Set<MatchDayTableDto> currentMatchDayTable = matchDayTableServiceDB.getCurrentMatchDayTable( matchDayId );
        List<MatchDayTableDto> updatedMatchTable = new ArrayList<>( currentMatchDayTable );

        matchDetailsList.forEach( matchDetail -> {
            if (isPlayerInTable( currentMatchDayTable, matchDetail.getPlayerId() )) {
                updateTableRow( matchDetail, updatedMatchTable );
                return;
            }
            updatedMatchTable.add( getNewRow( matchDetail ) );
        } );
        return updatedMatchTable;
    }

    private void updateTableRow(MatchDetailsDto matchDetail, List<MatchDayTableDto> updatedMatchTable) {
        updatedMatchTable.stream()
                .filter( tableRow -> tableRow.getPlayer().getId().equals( matchDetail.getPlayerId() ) )
                .findFirst()
                .ifPresent( row -> {
                    row.setCurrentRating( matchDetail.getMatchDayRatingAfterMatch() );
                    row.setMatches( row.getMatches().add( BigDecimal.ONE ) );
                    row.setPointsTotal( row.getPointsTotal().add( new BigDecimal( matchDetail.getMatchPoints() ) ) );
                    row.setMatchInProgress( matchDetail.getMatchInProgress() );
                } );
    }

    private MatchDayTableDto getNewRow(MatchDetailsDto matchDetail) {
        return MatchDayTableDto.builder()
                .matchDayId( matchDetail.getMatchDayId() )
                .player( playerServiceDB.getPlayer( matchDetail.getPlayerId() ) )
                .matches( BigDecimal.ONE )
                .pointsTotal( new BigDecimal( matchDetail.getMatchPoints() ) )
                .currentRating( null )
                .previousRating( matchDetail.getMatchDayRatingBeforeMatch() )
                .matchCurrentRating( BooleanUtils.isNotTrue( matchDetail.getMatchInProgress() ) ? null : matchDetail.getMatchDayRatingAfterMatch() )
                .isNewPlayer( true )
                .matchInProgress( matchDetail.getMatchInProgress() )
                .build();
    }

    private boolean isPlayerInTable(Set<MatchDayTableDto> currentMatchDayTable, Integer playerId) {
        return currentMatchDayTable.stream()
                .anyMatch( row -> row.getPlayer().getId().equals( playerId ) );
    }

    public List<MatchDayTableDto> getUpdatedTable(
            List<MatchDetailsDto> matchDetailsList,
            List<MatchDetailsDto> updatedMatchDetailsList,
            MatchDto matchDto,
            Boolean isComplete
    ) {
        List<MatchDayTableDto> matchDayTable = matchDayTableServiceDB.getMatchDayTable( matchDto.getMatchDayId() );
        List<MatchDayTableDto> updatedMatchTable = new ArrayList<>( matchDayTable );
        updateTableService.updateTable( updatedMatchTable, matchDetailsList, updatedMatchDetailsList,isComplete );
        return updatedMatchTable;
    }

    public List<MatchDayTableDto> updateBeforeRemoveMatch(List<MatchDetailsDto> matchDetailsList, Integer matchDayId) {
        List<MatchDayTableDto> matchDayTable = matchDayTableServiceDB.getMatchDayTable( matchDayId );
        List<MatchDayTableDto> updatedMatchTable = new ArrayList<>( matchDayTable );
        updateTableService.updateBeforeRemoveMatch( updatedMatchTable, matchDetailsList );
        return updatedMatchTable;
    }
}

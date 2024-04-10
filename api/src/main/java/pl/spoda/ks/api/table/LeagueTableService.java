package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.database.dto.LeagueTableDto;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.database.service.LeagueTableServiceDB;
import pl.spoda.ks.database.service.PlayerServiceDB;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LeagueTableService {


    private final LeagueTableServiceDB leagueTableServiceDB;
    private final PlayerServiceDB playerServiceDB;
    private final SortTableService sortTableService;
    private final UpdateTableService updateTableService;

    public List<LeagueTableDto> getCurrentTable(
            Integer leagueId,
            List<MatchDetailsDto> matchDetailsList,
            PointCountingMethod pointCountingMethod
    ) {
        Set<LeagueTableDto> currentLeagueTable = leagueTableServiceDB.getCurrentLeagueTable( leagueId );
        List<LeagueTableDto> updatedLeagueTable = new ArrayList<>( currentLeagueTable );

        matchDetailsList.forEach( matchDetail -> {
            if (isPlayerInTable( currentLeagueTable, matchDetail.getPlayerId() )) {
                updateTableRow( matchDetail, updatedLeagueTable );
            } else {
                updatedLeagueTable.add( getNewRow( matchDetail, currentLeagueTable.size(), pointCountingMethod ) );
            }
        } );
        return sortTableService.getSortedTable( updatedLeagueTable, pointCountingMethod, true );
    }

    private void updateTableRow(MatchDetailsDto matchDetail, List<LeagueTableDto> updatedLeagueTable) {
        updatedLeagueTable.stream()
                .filter( tableRow -> tableRow.getPlayer().getId().equals( matchDetail.getPlayerId() ) )
                .findFirst()
                .ifPresent( row -> {
                    row.setCurrentRating( matchDetail.getLeagueRatingAfterMatch() );
                    row.setMatches( row.getMatches().add( BigDecimal.ONE ) );
                    row.setPointsTotal( row.getPointsTotal().add( new BigDecimal( matchDetail.getMatchPoints() ) ) );
                    row.setMatchInProgress( matchDetail.getMatchInProgress() );
                } );
    }

    private LeagueTableDto getNewRow(MatchDetailsDto matchDetail, int tableSize, PointCountingMethod pointCountingMethod) {
        int defaultPosition = tableSize + 1;
        return LeagueTableDto.builder()
                .leagueId( matchDetail.getLeagueId() )
                .player( playerServiceDB.getPlayer( matchDetail.getPlayerId() ) )
                .matches( BigDecimal.ONE )
                .pointsTotal( new BigDecimal( matchDetail.getMatchPoints() ) )
                .currentPosition( defaultPosition )
                .previousPosition( defaultPosition )
                .standbyPosition( defaultPosition )
                .currentRating( preparePointCountingMethod( pointCountingMethod, matchDetail.getLeagueRatingAfterMatch() ) )
                .isNewPlayer( true )
                .matchInProgress( matchDetail.getMatchInProgress() )
                .build();
    }

    private BigDecimal preparePointCountingMethod(
            PointCountingMethod pointCountingMethod,
            BigDecimal ratingAfterMatch
    ) {
        return pointCountingMethod.equals( PointCountingMethod.RATING )
                ? ratingAfterMatch
                : null;
    }

    private boolean isPlayerInTable(Set<LeagueTableDto> currentTable, Integer playerId) {
        return currentTable.stream()
                .anyMatch( row -> row.getPlayer().getId().equals( playerId ) );
    }

    public List<LeagueTableDto> updateTable(
            List<MatchDetailsDto> matchDetailsList,
            List<MatchDetailsDto> updatedMatchDetailsList,
            MatchDayDto matchDayDto
    ) {
        List<LeagueTableDto> leagueTableDtoList = leagueTableServiceDB.getLeagueTable( matchDayDto.getSeason().getLeagueId() );
        List<LeagueTableDto> updatedLeagueTable = new ArrayList<>( leagueTableDtoList );
        PointCountingMethod pointCountingMethod = PointCountingMethod.RATING;
        updateTableService.updateTable( updatedLeagueTable, matchDetailsList, updatedMatchDetailsList, pointCountingMethod );
        return sortTableService.getSortedTable( updatedLeagueTable, pointCountingMethod, false );
    }

    public List<LeagueTableDto> updateBeforeRemoveMatch(List<MatchDetailsDto> matchDetailsList, Integer leagueId) {
        List<LeagueTableDto> leagueTable = leagueTableServiceDB.getLeagueTable( leagueId );
        List<LeagueTableDto> updatedLeagueTable = new ArrayList<>( leagueTable );
        updateTableService.updateBeforeRemoveMatch( updatedLeagueTable, matchDetailsList );
        return updatedLeagueTable;
    }
}

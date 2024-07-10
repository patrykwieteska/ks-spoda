package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final UpdateTableService updateTableService;

    public List<LeagueTableDto> getCurrentTableAfterMatchCreated(
            Integer leagueId,
            List<MatchDetailsDto> matchDetailsList
    ) {
        Set<LeagueTableDto> currentLeagueTable = leagueTableServiceDB.getCurrentLeagueTable( leagueId );
        List<LeagueTableDto> updatedLeagueTable = new ArrayList<>( currentLeagueTable );

        matchDetailsList.forEach( matchDetail -> {
            if (isPlayerInTable( currentLeagueTable, matchDetail.getPlayerId() )) {
                updateTableRow( matchDetail, updatedLeagueTable );
            } else {
                updatedLeagueTable.add( getNewRow( matchDetail ) );
            }
        } );
        return updatedLeagueTable;
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

    private LeagueTableDto getNewRow(MatchDetailsDto matchDetail) {
        return LeagueTableDto.builder()
                .leagueId( matchDetail.getLeagueId() )
                .player( playerServiceDB.getPlayer( matchDetail.getPlayerId() ) )
                .matches( BigDecimal.ONE )
                .pointsTotal( new BigDecimal( matchDetail.getMatchPoints() ) )
                .currentRating( null )
                .matchCurrentRating( matchDetail.getLeagueRatingAfterMatch() )
                .previousRating( matchDetail.getLeagueRatingBeforeMatch() )
                .isNewPlayer( true )
                .matchInProgress( matchDetail.getMatchInProgress() )
                .build();
    }

    private boolean isPlayerInTable(Set<LeagueTableDto> currentTable, Integer playerId) {
        return currentTable.stream()
                .anyMatch( row -> row.getPlayer().getId().equals( playerId ) );
    }

    public List<LeagueTableDto> getUpdatedTable(
            List<MatchDetailsDto> matchDetailsList,
            List<MatchDetailsDto> updatedMatchDetailsList,
            MatchDayDto matchDayDto,
            Boolean isComplete
    ) {
        List<LeagueTableDto> leagueTableDtoList = leagueTableServiceDB.getLeagueTable( matchDayDto.getSeason().getLeagueId() );
        List<LeagueTableDto> updatedLeagueTable = new ArrayList<>( leagueTableDtoList );
        updateTableService.updateTable( updatedLeagueTable, matchDetailsList, updatedMatchDetailsList, isComplete );
        return updatedLeagueTable;
    }

    public List<LeagueTableDto> updateBeforeRemoveMatch(List<MatchDetailsDto> matchDetailsList, Integer leagueId) {
        List<LeagueTableDto> leagueTable = leagueTableServiceDB.getLeagueTable( leagueId );
        List<LeagueTableDto> updatedLeagueTable = new ArrayList<>( leagueTable );
        updateTableService.updateBeforeRemoveMatch( updatedLeagueTable, matchDetailsList );
        return updatedLeagueTable;
    }
}

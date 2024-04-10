package pl.spoda.ks.api.table;

import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.table.CompetitionType;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.database.dto.TableBaseDto;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UpdateTableService {

    public <T extends TableBaseDto> void updateTable(
            List<T> tableList,
            List<MatchDetailsDto> oldDetailsList,
            List<MatchDetailsDto> updatedDetailsList,
            PointCountingMethod pointCountingMethod
    ) {
        List<Integer> matchDetailsPlayers = oldDetailsList.stream()
                .map( MatchDetailsDto::getPlayerId )
                .toList();

        tableList.stream()
                .filter( row -> matchDetailsPlayers.contains( row.getPlayer().getId() ) )
                .forEach( row -> updateRow( row, oldDetailsList, updatedDetailsList, pointCountingMethod ) );
    }

    private <T extends TableBaseDto> void updateRow(
            T row,
            List<MatchDetailsDto> oldDetailsList,
            List<MatchDetailsDto> updatedDetailsList,
            PointCountingMethod pointCountingMethod
    ) {
        MatchDetailsDto oldPlayerMatch = getPlayerMatchDetails( oldDetailsList, row.getPlayer() );
        MatchDetailsDto updatedPlayerMatch = getPlayerMatchDetails( updatedDetailsList, row.getPlayer() );
        BigDecimal updatedPointsValue = getUpdatedTotalPoints( row, oldPlayerMatch, updatedPlayerMatch );
        BigDecimal updatedRating = getUpdatedRatingValue( updatedPlayerMatch, row.getCompetitionType(), pointCountingMethod );

        row.setPointsTotal( updatedPointsValue );
        row.setCurrentRating( updatedRating );
        row.setMatchInProgress( updatedPlayerMatch.getMatchInProgress() );
    }

    private BigDecimal getUpdatedRatingValue(MatchDetailsDto updatedPlayerMatch, CompetitionType competitionType, PointCountingMethod pointCountingMethod) {
        if (!pointCountingMethod.equals( PointCountingMethod.RATING )) {
            return competitionType.equals( CompetitionType.LEAGUE )
                    ? updatedPlayerMatch.getLeagueRatingAfterMatch()
                    : null;
        }

        return switch (competitionType) {
            case LEAGUE -> updatedPlayerMatch.getLeagueRatingAfterMatch();
            case SEASON -> updatedPlayerMatch.getSeasonRatingAfterMatch();
            case MATCH_DAY -> updatedPlayerMatch.getMatchDayRatingAfterMatch();
        };

    }

    private static <T extends TableBaseDto> BigDecimal getUpdatedTotalPoints(T row, MatchDetailsDto oldPlayerMatch, MatchDetailsDto updatedPlayerMatch) {
        return row.getPointsTotal()
                .subtract( new BigDecimal( oldPlayerMatch.getMatchPoints() ) )
                .add( new BigDecimal( updatedPlayerMatch.getMatchPoints() ) );
    }

    private MatchDetailsDto getPlayerMatchDetails(List<MatchDetailsDto> matchDetailsList, PlayerDto playerRow) {
        return matchDetailsList.stream()
                .filter( details -> details.getPlayerId().equals( playerRow.getId() ) )
                .findFirst()
                .orElseThrow( () -> new SpodaApplicationException( "Player not found in the table" ) );
    }

    public <T extends TableBaseDto> void updateBeforeRemoveMatch(
            List<T> table,
            List<MatchDetailsDto> matchDetailsList
    ) {
        List<Integer> matchDetailsPlayers = matchDetailsList.stream()
                .map( MatchDetailsDto::getPlayerId )
                .toList();

        table.stream()
                .filter( row -> matchDetailsPlayers.contains( row.getPlayer().getId() ) )
                .forEach( row -> updateBeforeDelete( row, matchDetailsList ) );

    }

    private <T extends TableBaseDto> void updateBeforeDelete(
            T row, List<MatchDetailsDto> matchDetailsList
    ) {


        MatchDetailsDto matchPlayerDetails = getPlayerMatchDetails( matchDetailsList, row.getPlayer() );
        BigDecimal updatedRating = getRatingBefore( matchPlayerDetails, row.getCompetitionType() );
        BigDecimal updatedPointsTotal = row.getPointsTotal().subtract( new BigDecimal( matchPlayerDetails.getMatchPoints() ) );
        row.setCurrentRating( updatedRating );
        row.setCurrentPosition( row.getPreviousPosition() );
        row.setPreviousPosition( row.getStandbyPosition() );
        row.setMatches( row.getMatches().subtract( BigDecimal.ONE ) );
        row.setPointsTotal( updatedPointsTotal );
        row.setMatchInProgress( false );
    }

    private BigDecimal getRatingBefore(
            MatchDetailsDto updatedPlayerMatch,
            CompetitionType competitionType
    ) {
        return switch (competitionType) {
            case LEAGUE -> updatedPlayerMatch.getLeagueRatingBeforeMatch();
            case SEASON -> updatedPlayerMatch.getSeasonRatingBeforeMatch();
            case MATCH_DAY -> updatedPlayerMatch.getMatchDayRatingBeforeMatch();
        };

    }
}

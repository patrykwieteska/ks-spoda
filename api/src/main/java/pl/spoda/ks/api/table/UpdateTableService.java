package pl.spoda.ks.api.table;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.comons.table.CompetitionType;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.database.dto.PlayerDto;
import pl.spoda.ks.database.dto.TableBaseDto;
import pl.spoda.ks.database.service.MatchDetailsServiceDB;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateTableService {


    private final MatchDetailsServiceDB matchDetailsServiceDB;

    /*
     * Podczas zapisywania tabel nie zapisujemy pozycji gracza w tabeli.
     * Podczas odczytywania tabel sortujemy i generujemy pozycje w tabeli.
     */

    @Value("${application.initial-rating}")
    public String initialRating;

    public <T extends TableBaseDto> void updateTable(
            List<T> tableList,
            List<MatchDetailsDto> oldDetailsList,
            List<MatchDetailsDto> updatedDetailsList,
            Boolean isComplete
    ) {
        List<Integer> matchDetailsPlayers = oldDetailsList.stream()
                .map( MatchDetailsDto::getPlayerId )
                .toList();

        tableList.stream()
                .filter( row -> matchDetailsPlayers.contains( row.getPlayer().getId() ) )
                .forEach( row -> updateRow( row, oldDetailsList, updatedDetailsList, isComplete ) );
    }

    private <T extends TableBaseDto> void updateRow(
            T row,
            List<MatchDetailsDto> oldDetailsList,
            List<MatchDetailsDto> updatedDetailsList,
            Boolean isComplete
    ) {
        MatchDetailsDto oldPlayerMatch = getPlayerMatchDetails( oldDetailsList, row.getPlayer() );
        MatchDetailsDto updatedPlayerMatch = getPlayerMatchDetails( updatedDetailsList, row.getPlayer() );
        BigDecimal updatedPointsValue = getUpdatedTotalPoints( row, oldPlayerMatch, updatedPlayerMatch );
        BigDecimal currentRating = getUpdatedRatingValue( updatedPlayerMatch, row.getCompetitionType() );

        if (BooleanUtils.isTrue( isComplete )) {
            row.setCurrentRating( currentRating );
            row.setMatchCurrentRating( null );
        } else {
            row.setMatchCurrentRating( currentRating );
        }

        row.setPreviousRating( getRatingBefore( updatedPlayerMatch,row.getCompetitionType() ) );
        row.setPointsTotal( updatedPointsValue );
        row.setMatchInProgress( updatedPlayerMatch.getMatchInProgress() );
    }

    private BigDecimal getUpdatedRatingValue(MatchDetailsDto updatedPlayerMatch, CompetitionType competitionType) {
        return switch (competitionType) {
            case LEAGUE -> updatedPlayerMatch.getLeagueRatingAfterMatch();
            case SEASON -> updatedPlayerMatch.getSeasonRatingAfterMatch();
            case MATCH_DAY -> updatedPlayerMatch.getMatchDayRatingAfterMatch();
        };

    }

    private <T extends TableBaseDto> BigDecimal getUpdatedTotalPoints(T row, MatchDetailsDto oldPlayerMatch, MatchDetailsDto updatedPlayerMatch) {
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
            T tableBaseRow,
            List<MatchDetailsDto> matchDetailsList
    ) {
        MatchDetailsDto matchPlayerDetails = getPlayerMatchDetails( matchDetailsList, tableBaseRow.getPlayer() );
        BigDecimal updatedCurrentRating = getRatingBefore( matchPlayerDetails, tableBaseRow.getCompetitionType() );
        BigDecimal updatedPreviousRating = getPlayerPreviousRatingFromPreviousMatch( matchPlayerDetails, tableBaseRow.getCompetitionType() );
        BigDecimal updatedPointsTotal = tableBaseRow.getPointsTotal().subtract( new BigDecimal( matchPlayerDetails.getMatchPoints() ) );
        tableBaseRow.setCurrentRating( updatedCurrentRating );
        tableBaseRow.setPreviousRating( updatedPreviousRating );
        tableBaseRow.setMatches( tableBaseRow.getMatches().subtract( BigDecimal.ONE ) );
        tableBaseRow.setPointsTotal( updatedPointsTotal );
        tableBaseRow.setMatchInProgress( false );
    }

    private BigDecimal getPlayerPreviousRatingFromPreviousMatch(MatchDetailsDto matchPlayerDetails, CompetitionType competitionType) {
        Optional<MatchDetailsDto> previousLeagueMatchDetails = matchDetailsServiceDB.getPlayerPreviousLeagueMatch(
                matchPlayerDetails.getPlayerId(),
                matchPlayerDetails.getLeagueId(),
                matchPlayerDetails.getMatchId()
        );

        BigDecimal initialRatingValue = new BigDecimal( initialRating );
        return switch (competitionType) {
            case LEAGUE -> previousLeagueMatchDetails.map( MatchDetailsDto::getLeagueRatingBeforeMatch ).orElse( initialRatingValue );
            case SEASON -> previousLeagueMatchDetails.filter( x-> x.getSeasonId().equals( matchPlayerDetails.getSeasonId() ) ).map( MatchDetailsDto::getSeasonRatingBeforeMatch ).orElse( initialRatingValue );
            case MATCH_DAY -> previousLeagueMatchDetails.filter( x-> x.getMatchDayId().equals( matchPlayerDetails.getMatchDayId() ) ).map( MatchDetailsDto::getMatchDayRatingBeforeMatch ).orElse( initialRatingValue );
        };
    }

    private BigDecimal getRatingBefore(
            MatchDetailsDto matchDetailsDto,
            CompetitionType competitionType
    ) {
        return switch (competitionType) {
            case LEAGUE -> matchDetailsDto.getLeagueRatingBeforeMatch();
            case SEASON -> matchDetailsDto.getSeasonRatingBeforeMatch();
            case MATCH_DAY -> matchDetailsDto.getMatchDayRatingBeforeMatch();
        };

    }
}

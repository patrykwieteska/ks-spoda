package pl.spoda.ks.api.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.season.enums.PointCountingMethod;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadMatchRequestMapper {

    private static Integer lastCorrectUploadId = 0;

    @Value("${match-import.path}")
    public String uploadPath = "";

    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

    public List<UploadRequest> prepareMatchRequestList() throws IOException {
        List<List<String>> records = new ArrayList<>();

        try (InputStream inputStream = getClass().getResourceAsStream( uploadPath )) {
            assert inputStream != null;
            BufferedReader br = new BufferedReader( new InputStreamReader( inputStream ) );
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split( DELIMITER );
                records.add( Arrays.asList( values ) );
            }
        }

        if (records.isEmpty()) {
            throw new SpodaApplicationException( "Empty upload file" );
        }

        return mapToRequestList( records );
    }

    private List<UploadRequest> mapToRequestList(List<List<String>> records) {
        return records.stream()
                .map( this::mapToUploadRequest )
                .toList();
    }

    private UploadRequest mapToUploadRequest(List<String> csvRow) {
        return UploadRequest.builder()
                .homePlayers( getIntegerList( csvRow, 0 ) )
                .awayPlayers( getIntegerList( csvRow, 1 ) )
                .homeGoals( getIntegerValueOrThrow( csvRow, 2 ) )
                .awayGoals( getIntegerValueOrThrow( csvRow, 3 ) )
                .arePenalties( getBooleanValueOrNull( csvRow, 4 ) )
                .penaltiesHome( getIntegerValueOrNull( csvRow, 5 ) )
                .penaltiesAway( getIntegerValueOrNull( csvRow, 6 ) )
                .homeTeamId( getIntegerValueOrThrow( csvRow, 7 ) )
                .awayTeamId( getIntegerValueOrThrow( csvRow, 8 ) )
                .isPlayOff( getBooleanValueOrNull( csvRow, 9 ) )
                .matchTime( getMatchTime( csvRow, 10 ) )
                .euroMatchId( getIntegerValueOrNull( csvRow, 11 ) )
                .isNewMatchDay( getBooleanValueOrNull( csvRow, 12 ) )
                .isNewSeason( getBooleanValueOrNull( csvRow, 13 ) )
                .isNewLeague( getBooleanValueOrNull( csvRow, 14 ) )
                .matchDayLocation( getStringValue( csvRow, 15 ) )
                .seasonName( getStringValue( csvRow, 16 ) )
                .seasonImage( getStringValue( csvRow, 17 ) )
                .leagueName( getStringValue( csvRow, 18 ) )
                .leagueImage( getStringValue( csvRow, 19 ) )
                .matchWeightIndex( getDecimalValue( csvRow, 20 ) )
                .matchDayStartDate( getDateValue( csvRow, 21 ) )
                .seasonStartDate( getDateValue( csvRow, 22 ) )
                .leagueStartDate( getDateValue( csvRow, 23 ) )
                .isEuro( getBooleanValueOrNull( csvRow, 24 ) )
                .matchCommentary( getStringValue( csvRow, 25 ) )
                .pointCountingMethod( PointCountingMethod.getByName( getStringValue( csvRow, 26 ) ) )
                .matchDayTitle( getStringValue( csvRow, 27 ) )
                .matchDayHeaderImg( getStringValue( csvRow, 28 ) )
                .uploadId( getUploadId( getIntegerValueOrThrow( csvRow, 29 ) ) )
                .build();
    }

    private static Integer getUploadId(Integer integerValueOrThrow) {
        lastCorrectUploadId = integerValueOrThrow;
        return integerValueOrThrow;
    }

    private LocalDate getDateValue(List<String> csvRow, int index) {
        String matchDate = csvRow.get( index );
        if (matchDate.isEmpty()) {
            return null;
        }

        return LocalDate.parse( matchDate, DATE_FORMATTER );
    }

    private BigDecimal getDecimalValue(List<String> csvRow, int index) {
        String value = csvRow.get( index );
        if (value.isEmpty()) {
            return null;
        }
        return new BigDecimal( value );
    }

    private LocalDateTime getMatchTime(List<String> csvRow, int index) {
        String matchTimeValue = csvRow.get( index );
        if (matchTimeValue.isEmpty()) {
            throw new SpodaApplicationException( "Match date is null" );
        }
        return LocalDateTime.parse( matchTimeValue, DATE_TIME_FORMATTER );
    }

    private String getStringValue(List<String> csvRow, int index) {
        String value = csvRow.get( index );
        return !value.isEmpty()
                ? value
                : null;
    }

    private Boolean getBooleanValueOrNull(List<String> csvRow, int index) {
        String value = csvRow.get( index );
        if (value.isEmpty()) {
            return false;
        }
        return switch (value.toUpperCase()) {
            case "1", "TRUE" -> true;
            case "0", "FALSE" -> false;
            default -> null;
        };
    }

    private Integer getIntegerValueOrNull(List<String> csvRow, int index) {
        String value = csvRow.get( index );
        if (value.isEmpty()) {
            return null;
        }
        return Integer.parseInt( value );
    }

    private Integer getIntegerValueOrThrow(List<String> value, int index) {
        String stringValue = value.get( index );
        if (stringValue.isEmpty()) {
            throw new SpodaApplicationException( "value cannot be empty: " + lastCorrectUploadId );
        }
        return Integer.parseInt( stringValue );
    }

    private List<Integer> getIntegerList(List<String> value, int index) {
        return Arrays.stream( value.get( index ).split( "," ) ).map( Integer::parseInt ).toList();
    }
}

package pl.spoda.ks.database.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.mapstruct.control.DeepClone;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@DeepClone
public class MatchDetailsDto extends BaseDto {

    private Integer id;
    private Integer matchId;
    private Integer matchDayId;
    private Integer seasonId;
    private Integer leagueId;
    private Integer playerId;
    private Integer matchPoints;
    private Integer goalsScored;
    private Integer goalsConceded;
    private String matchResult;
    private BigDecimal leagueRatingBeforeMatch;
    private BigDecimal leagueRatingAfterMatch;
    private BigDecimal seasonRatingBeforeMatch;
    private BigDecimal seasonRatingAfterMatch;
    private BigDecimal matchDayRatingBeforeMatch;
    private BigDecimal matchDayRatingAfterMatch;
    private Boolean matchInProgress;
}

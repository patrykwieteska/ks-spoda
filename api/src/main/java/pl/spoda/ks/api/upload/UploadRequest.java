package pl.spoda.ks.api.upload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.spoda.ks.api.season.enums.PointCountingMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadRequest {

    private List<Integer> homePlayers;
    private List<Integer> awayPlayers;
    private Integer homeGoals;
    private Integer awayGoals;
    private Boolean arePenalties;
    private Integer penaltiesHome;
    private Integer penaltiesAway;
    private Integer homeTeamId;
    private Integer awayTeamId;
    private Boolean isPlayOff;
    private LocalDateTime matchTime;
    private Integer euroMatchId;
    private Boolean isNewMatchDay;
    private Boolean isNewSeason;
    private Boolean isNewLeague;
    private String matchDayLocation;
    private String seasonName;
    private String seasonImage;
    private String leagueName;
    private String leagueImage;
    private BigDecimal matchWeightIndex;
    private LocalDate matchDayStartDate;
    private LocalDate seasonStartDate;
    private LocalDate leagueStartDate;
    private Boolean isEuro;
    private String matchCommentary;
    private PointCountingMethod pointCountingMethod;
    private String matchDayTitle;
    private String matchDayHeaderImg;
    private Integer uploadId;

}

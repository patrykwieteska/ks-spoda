package pl.spoda.ks.api.season.model.init;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import pl.spoda.ks.api.matchday.model.init.MatchDayData;

import java.time.LocalDate;
import java.util.List;

@Data
@ToString
@Builder
public class SeasonData {

    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isFinished;
    private List<MatchDayData> matchDays;
    private Integer seasonId;


}

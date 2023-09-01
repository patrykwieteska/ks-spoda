package pl.spoda.ks.api.matchday.model.init;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@Builder
public class MatchDayData {

    private Integer seasonMatchDay;
    private LocalDate date;
    private String location;
    private Boolean isFinished;
    private Integer matchDayId;
}

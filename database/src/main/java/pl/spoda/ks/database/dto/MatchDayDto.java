package pl.spoda.ks.database.dto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MatchDayDto {

    private Integer id;
    private Integer seasonId;
    private LocalDate date;
    private Integer seasonMatchDay;
    private String location;
    private Boolean isFinished;
    private List<MatchDto> matchList;

}

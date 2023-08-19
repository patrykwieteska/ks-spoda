package pl.spoda.ks.database.dto;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MatchDayDto {

    private Integer id;
    private Integer leagueId;
    private LocalDate date;
    private Integer leagueMatchDayNumber;
    private String location;
    private Boolean isFinished;

}

package pl.spoda.ks.database.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class MatchDayDto extends BaseDto {

    private Integer id;
    private Integer seasonId;
    private LocalDate date;
    private Integer seasonMatchDay;
    private String location;
    private Boolean isFinished;
    private List<MatchDto> matchList;
    private SeasonDto season;
    private String title;
    private String headerImg;

}

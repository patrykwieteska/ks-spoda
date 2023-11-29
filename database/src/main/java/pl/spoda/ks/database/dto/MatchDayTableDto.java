package pl.spoda.ks.database.dto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.comons.table.Competition;
import pl.spoda.ks.comons.table.CompetitionType;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class MatchDayTableDto extends TableBaseDto implements Competition {

    private Integer id;
    private Integer matchDayId;

    @Override
    public CompetitionType getCompetitionType() {
        return CompetitionType.MATCH_DAY;
    }
}

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
@Setter
public class SeasonTableDto extends TableBaseDto implements Competition {

    private Integer id;
    private Integer seasonId;

    @Override
    public CompetitionType getCompetitionType() {
        return CompetitionType.SEASON;
    }
}

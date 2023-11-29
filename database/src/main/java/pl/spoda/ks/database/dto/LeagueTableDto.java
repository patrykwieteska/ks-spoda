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
public class LeagueTableDto extends TableBaseDto implements Competition {

    private Integer id;
    private Integer leagueId;

    @Override
    public CompetitionType getCompetitionType() {
        return CompetitionType.LEAGUE;
    }
}

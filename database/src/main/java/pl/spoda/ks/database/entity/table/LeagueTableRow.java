package pl.spoda.ks.database.entity.table;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.database.entity.League;

@ToString(callSuper = true)
@Table(name = "LEAGUE_TABLE_ROW")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
public class LeagueTableRow extends TableBase {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="LEAGUE_ID",nullable = false,insertable = false,updatable = false)
    private Integer leagueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LEAGUE_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private League league;
}

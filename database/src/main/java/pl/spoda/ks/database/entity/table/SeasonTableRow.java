package pl.spoda.ks.database.entity.table;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.database.entity.Season;

@ToString(callSuper = true)
@Table(name = "SEASON_TABLE_ROW")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
public class SeasonTableRow extends TableBase {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="SEASON_ID",nullable = false,insertable = false,updatable = false)
    private Integer seasonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SEASON_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Season season;

}

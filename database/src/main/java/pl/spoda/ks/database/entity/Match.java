package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@ToString(callSuper = true)
@Table(name = "MATCH")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class Match extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "MATCH_DAY_ID", nullable = false, insertable = false, updatable = false)
    private Integer matchDayId;
    @Column(name = "MATCH_TIME", nullable = false)
    private LocalDateTime matchTime;
    @Column(name = "HOME_TEAM_ID", nullable = false, insertable = false, updatable = false)
    private Integer homeTeamId;
    @Column(name = "AWAY_TEAM_ID", nullable = false, insertable = false, updatable = false)
    private Integer awayTeamId;
    @Column(name = "HOME_GOALS")
    private Integer homeGoals;
    @Column(name = "AWAY_GOALS")
    private Integer awayGoals;
    @Column(name = "IS_FINISHED")
    private Boolean isFinished;
    @Column(name = "TYPE")
    private String type;

    @ManyToOne
    @JoinColumn(name = "MATCH_DAY_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MatchDay matchDay;


}

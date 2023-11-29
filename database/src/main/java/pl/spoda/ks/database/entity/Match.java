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
@Setter
public class Match extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "MATCH_TIME", nullable = false)
    private LocalDateTime matchTime;
    @Column(name = "HOME_GOALS", nullable = false)
    private Integer homeGoals;
    @Column(name = "AWAY_GOALS", nullable = false)
    private Integer awayGoals;
    @Column(name = "IS_FINISHED")
    private Boolean isFinished;

    @ManyToOne
    @JoinColumn(name = "MATCH_DAY_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MatchDay matchDay;

    @OneToOne(cascade = CascadeType.ALL) @JoinColumn( name = "home_team_id" )
    private MatchTeam homeTeam;

    @OneToOne(cascade = CascadeType.ALL) @JoinColumn( name = "away_team_id" )
    private MatchTeam awayTeam;

}

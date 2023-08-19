package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@ToString(callSuper = true)
@Table(name = "ROUND")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class MatchDay extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="LEAGUE_ID",nullable = false,insertable = false,updatable = false)
    private Integer leagueId;
    @Column(name = "DATE", nullable = false)
    private LocalDate date;
    @Column(name="LOCATION")
    private String location;
    @Column(name="IS_FINISHED")
    private Boolean isFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LEAGUE_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private League league;

    public MatchDay league(League league) {
        this.league = league;
        return this;
    }

    public MatchDay isFinished(Boolean isFinished) {
        this.isFinished = isFinished;
        return this;
    }

}

package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@ToString(callSuper = true)
@Table(name = "SEASON")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class Season extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="LEAGUE_ID",nullable = false,insertable = false,updatable = false)
    private Integer leagueId;
    @Column(name = "START_DATE")
    private LocalDate startDate;
    @Column(name = "END_DATE")
    private LocalDate endDate;
    @Column(name = "IS_FINISHED")
    private Boolean isFinished;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<MatchDay> matchDayList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LEAGUE_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private League league;

    public Season isFinished(boolean isFinished) {
        this.isFinished=isFinished;
        return this;
    }

    public Season endDate(LocalDate endDate) {
        this.endDate=endDate;
        return this;
    }

    public Season league(League league) {
        this.league=league;
        return this;
    }
}

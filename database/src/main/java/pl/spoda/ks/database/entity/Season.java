package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ToString(callSuper = true)
@Table(name = "SEASON")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
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
    @Column(name = "INITIAL_RATING",updatable = false)
    private BigDecimal initialRating;
    @Column(name="POINT_COUNTING_METHOD")
    private String pointCountingMethod;
    @Column(name="RATING_TYPE")
    private String ratingType;
    @Column(name="LEAGUE_SEASON_COUNT")
    private Integer leagueSeasonCount;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
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

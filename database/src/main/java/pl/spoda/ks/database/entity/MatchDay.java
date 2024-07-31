package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true)
@Table(name = "MATCH_DAY")
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
    @Column(name="SEASON_ID",nullable = false,insertable = false,updatable = false)
    private Integer seasonId;
    @Column(name = "DATE", nullable = false)
    private LocalDate date;
    @Column(name="LOCATION")
    private String location;
    @Column(name="IS_FINISHED")
    private Boolean isFinished;
    @Column(name="SEASON_MATCH_DAY")
    private Integer seasonMatchDay;
    @Column(name="TITLE")
    private String title;
    @Column(name="HEADER_IMG")
    private String headerImg;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="SEASON_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Season season;

    @OneToMany(mappedBy = "matchDay", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Match> matchList;

    public MatchDay setMatchList(List<Match> matchList) {
        this.matchList = matchList;
        return this;
    }

    public MatchDay season(Season season) {
        this.season = season;
        return this;
    }

    public MatchDay isFinished(Boolean isFinished) {
        this.isFinished = isFinished;
        return this;
    }

    public MatchDay seasonMatchDay(Integer seasonMatchDay) {
        this.seasonMatchDay = seasonMatchDay;
        return this;
    }

    public MatchDay addToMatchList(Match match) {
        if (this.matchList.isEmpty()) {
            this.matchList = new ArrayList<>();
        }
        this.matchList.add( match );
        return this;
    }
}

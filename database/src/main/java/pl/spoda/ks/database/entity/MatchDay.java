package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SEASON_ID")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Season season;

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
}

package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@ToString(callSuper = true)
@Table(name = "MATCH_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
public class MatchDetails extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="MATCH_ID")
    private Integer matchId;
    @Column(name="MATCH_DAY_ID")
    private Integer matchDayId;
    @Column(name="SEASON_ID")
    private Integer seasonId;
    @Column(name="LEAGUE_ID")
    private Integer leagueId;
    @Column(name="PLAYER_ID")
    private Integer playerId;
    @Column(name="MATCH_POINTS")
    private Integer matchPoints;
    @Column(name="GOALS_SCORED")
    private Integer goalsScored;
    @Column(name="GOALS_CONCEDED")
    private Integer goalsConceded;
    @Column(name="MATCH_RESULT")
    private String matchResult;
    @Column(name="LEAGUE_RATING_BEFORE_MATCH")
    private BigDecimal leagueRatingBeforeMatch;
    @Column(name="LEAGUE_RATING_AFTER_MATCH")
    private BigDecimal leagueRatingAfterMatch;
    @Column(name="SEASON_RATING_BEFORE_MATCH")
    private BigDecimal seasonRatingBeforeMatch;
    @Column(name="SEASON_RATING_AFTER_MATCH")
    private BigDecimal seasonRatingAfterMatch;
    @Column(name="MATCH_DAY_RATING_BEFORE_MATCH")
    private BigDecimal matchDayRatingBeforeMatch;
    @Column(name="MATCH_DAY_RATING_AFTER_MATCH")
    private BigDecimal matchDayRatingAfterMatch;
    @Column(name="MATCH_IN_PROGRESS")
    private Boolean matchInProgress;
}

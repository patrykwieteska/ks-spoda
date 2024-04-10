package pl.spoda.ks.database.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@MappedSuperclass
@ToString(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@RequiredArgsConstructor
public class TableBase extends BaseEntity {

    @Column(name = "CURRENT_RATING", nullable = false)
    private BigDecimal currentRating;

    @Column(name = "CURRENT_POSITION")
    private Integer currentPosition;

    @Column(name = "PREVIOUS_POSITION")
    private Integer previousPosition;

    @Column(name = "STANDBY_POSITION")
    private Integer standbyPosition;

    @Column(name = "MATCHES")
    private BigDecimal matches;

    @Column(name = "POINTS_TOTAL")
    private BigDecimal pointsTotal;

    @Column(name = "MATCH_IN_PROGRESS")
    private Boolean matchInProgress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    @ToString.Exclude
    private Player player;
}

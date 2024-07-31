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

    @Column(name = "PREVIOUS_RATING", nullable = false)
    private BigDecimal previousRating;

    @Column(name = "MATCH_CURRENT_RATING", nullable = false)
    private BigDecimal matchCurrentRating;

    @Column(name = "MATCHES")
    private BigDecimal matches;

    @Column(name = "POINTS_TOTAL")
    private BigDecimal pointsTotal;

    @Column(name = "MATCH_IN_PROGRESS")
    private Boolean matchInProgress;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    @ToString.Exclude
    private Player player;
}

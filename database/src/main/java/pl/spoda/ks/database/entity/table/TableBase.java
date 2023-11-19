package pl.spoda.ks.database.entity.table;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.database.entity.BaseEntity;

import java.math.BigDecimal;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TableBase extends BaseEntity {

    @Column(name="PLAYER_ID")
    private Integer playerId;
    @Column(name="MATCHES")
    private Integer matches;
    @Column(name="RATING")
    private BigDecimal rating;
    @Column(name="POINTS_TOTAL")
    private Integer pointsTotal;
    @Column(name="WINS")
    private Integer wins;
    @Column(name="DRAWS")
    private Integer draws;
    @Column(name="LOSES")
    private Integer loses;
    @Column(name="GOALS_SCORED")
    private Integer goalScored;
    @Column(name="GOALS_CONCEDED")
    private Integer goalsConceded;
    @Column(name="PLAYER_FORM")
    private String playerForm;
}

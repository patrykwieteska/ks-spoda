package pl.spoda.ks.database.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@ToString(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@MappedSuperclass
public abstract class Rating extends BaseEntity{

    @Column(name="PLAYER_ID",nullable = false)
    private Integer playerId;

    @Column(name="CURRENT_RATING",nullable = false)
    private BigDecimal currentRating;

    @Column(name="PREVIOUS_RATING",nullable = false)
    private BigDecimal previousRating;

}

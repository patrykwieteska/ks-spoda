package pl.spoda.ks.database.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Table(name = "SEASON_RATING")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
@Setter
public class SeasonRating extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "SEASON_ID", nullable = false)
    private Integer seasonId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="player_id", referencedColumnName = "id")
    @ToString.Exclude
    private Player player;

}

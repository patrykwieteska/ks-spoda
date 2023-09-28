package pl.spoda.ks.database.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Table(name = "LEAGUE_RATING")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class LeagueRating extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "LEAGUE_ID", nullable = false)
    private Integer leagueId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="player_id", referencedColumnName = "id")
    @ToString.Exclude
    private Player player;

}

package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Table(name = "LEAGUES_PLAYERS")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
public class LeaguesPlayers extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "league_id")
    private Integer leagueId;

    @Column(name = "player_id")
    private Integer playerId;
}



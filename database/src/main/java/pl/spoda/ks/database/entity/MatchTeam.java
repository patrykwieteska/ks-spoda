package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@ToString(callSuper = true)
@Table(name = "MATCH_TEAM")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class MatchTeam extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="GAME_TEAM_ID")
    private Integer gameTeamId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="MATCH_TEAM_PLAYER",
            joinColumns = @JoinColumn(name="match_team_id"),
            inverseJoinColumns = @JoinColumn(name="player_id"))
    @ToString.Exclude
    private Set<Player> teamPlayers;

}

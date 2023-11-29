package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Table(name = "MATCH_TEAM_PLAYER")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class MatchTeamPlayer extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="PLAYER_ID", nullable = false)
    private Integer playerId;
    @Column(name="MATCH_TEAM_ID", nullable = false)
    private Integer matchTeamId;
}

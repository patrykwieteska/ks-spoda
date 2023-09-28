package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    @Column(name="FIRST_PLAYER_ID",nullable = false,insertable = false,updatable = false)
    private Integer firstPlayerId;
    @Column(name="SECOND_PLAYER_ID", nullable = true,insertable = false,updatable = false)
    private LocalDateTime secondPlayerId;
    @Column(name="GAME_TEAM_ID", nullable = true,insertable = false,updatable = false)
    private Integer gameTeamId;


}

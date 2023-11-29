package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Table(name = "GAME_TEAM")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class GameTeam extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="NAME", nullable = false)
    private String name;
    @Column(name="SHORT_NAME", nullable = false)
    private String shortName;
    @Column(name="ALIAS")
    private String teamAlias;
    @Column(name="OVERALL_RATING")
    private Integer overallRating;
    @Column(name = "BADGE_IMG")
    private String badgeImg;

}

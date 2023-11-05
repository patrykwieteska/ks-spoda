package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;
import java.util.Set;

@ToString(callSuper = true)
@Table(name = "LEAGUE")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
public class League extends BaseEntity {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "LOGO_URL")
    private String logoUrl;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "TEAM_STRUCTURE")
    private String teamStructure;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Season> seasonList;

    @ManyToMany(mappedBy = "leagues")
    @ToString.Exclude
    private Set<Player> players;

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}

package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import pl.spoda.ks.database.entity.table.LeagueTableRow;

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
    @Column(name="IS_PRIVATE")
    private Boolean isPrivate;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Season> seasons;

    @ManyToMany(mappedBy = "leagues")
    @ToString.Exclude
    private Set<Player> players;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<LeagueTableRow> leagueTableRowList;

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}

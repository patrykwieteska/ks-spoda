package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;
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
    @Column(name = "IS_PRIVATE")
    private Boolean isPrivate;
    @Column(name = "START_DATE")
    private LocalDate startDate;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private List<Season> seasons;

    @ManyToMany(mappedBy = "leagues", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Player> players;

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}

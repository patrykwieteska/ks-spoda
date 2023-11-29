package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Table(name = "LEAGUE_TABLE")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
public class LeagueTable extends TableBase {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "LEAGUE_ID")
    private Integer leagueId;

}



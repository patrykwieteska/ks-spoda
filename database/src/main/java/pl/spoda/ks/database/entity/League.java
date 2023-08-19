package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

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
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "START_DATE")
    private LocalDate startDate;
    @Column(name = "END_DATE")
    private LocalDate endDate;
    @Column(name = "IS_FINISHED")
    private Boolean isFinished;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<MatchDay> matchDayList;

    public League matchDayList(List<MatchDay> matchDayList) {
        this.matchDayList = matchDayList;
        return this;
    }

    public League isFinished(Boolean isFinished) {
        this.isFinished = isFinished;
        return this;
    }

    public League endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }
}

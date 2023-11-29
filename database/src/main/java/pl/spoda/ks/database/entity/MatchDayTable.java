package pl.spoda.ks.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@ToString(callSuper = true)
@Table(name = "MATCH_DAY_TABLE")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
public class MatchDayTable extends TableBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "MATCH_DAY_ID", nullable = false)
    private Integer matchDayId;

}

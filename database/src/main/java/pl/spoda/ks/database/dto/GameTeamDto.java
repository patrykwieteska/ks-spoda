package pl.spoda.ks.database.dto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class GameTeamDto extends BaseDto {

    private Integer id;
    private String name;
    private String shortName;
    private String teamAlias;
    private Integer overallRating;
    private String badgeImg;
}

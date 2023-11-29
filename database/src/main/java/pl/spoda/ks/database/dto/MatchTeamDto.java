package pl.spoda.ks.database.dto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class MatchTeamDto extends BaseDto {

    private Integer id;
    private Integer gameTeamId;
    private Set<PlayerDto> teamPlayers;
}

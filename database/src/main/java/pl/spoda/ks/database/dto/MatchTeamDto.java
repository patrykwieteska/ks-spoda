package pl.spoda.ks.database.dto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MatchTeamDto {

    private Integer id;
    private Integer firstPlayerId;
    private LocalDateTime secondPlayerId;
    private Integer gameTeamId;
}

package pl.spoda.ks.database.dto;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RoundDto {

    private Integer id;
    private Integer leagueId;
    private LocalDate date;

}

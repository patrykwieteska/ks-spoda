package pl.spoda.ks.database.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LeagueDto {

    private Integer id;
    private String name;
    private String logoUrl;
    private String description;
    private String type;
    private String teamStructure;
    private LocalDateTime creationDate;
    private Set<PlayerDto> playerList;
    private Boolean isPrivate;
    private List<SeasonDto> seasonList;
    private List<LeagueTableRowDto> leagueTableRowList;

}

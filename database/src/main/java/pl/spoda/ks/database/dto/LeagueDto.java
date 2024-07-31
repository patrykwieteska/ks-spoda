package pl.spoda.ks.database.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class LeagueDto extends BaseDto {

    private Integer id;
    private String name;
    private String logoUrl;
    private String description;
    private String type;
    private String teamStructure;
    private LocalDateTime creationDate;
    private Set<PlayerDto> playerList;
    private Boolean isPrivate;
    private LocalDate startDate;

}

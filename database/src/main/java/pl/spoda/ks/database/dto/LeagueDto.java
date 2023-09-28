package pl.spoda.ks.database.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LeagueDto {

    private Integer id;
    private String name;
    private String description;
    private String type;
    private String teamStructure;

}

package pl.spoda.ks.api.match.model;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameTeam {

    private Integer id;
    private String name;
    private String badgeImg;
}

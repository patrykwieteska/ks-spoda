package pl.spoda.ks.api.player.model;

import lombok.*;

@Data
@ToString
@Builder
public class PlayerData {

    private Integer id;
    private String name;
    private String alias;
}

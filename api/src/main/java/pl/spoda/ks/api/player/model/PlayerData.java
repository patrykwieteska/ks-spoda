package pl.spoda.ks.api.player.model;

import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@Builder
public class PlayerData {

    private Integer id;
    private String name;
    private String alias;
    private String playerImg;
    private String desc;
    private LocalDate joinDate;
}

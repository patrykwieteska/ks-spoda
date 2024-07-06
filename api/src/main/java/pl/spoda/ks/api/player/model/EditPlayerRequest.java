package pl.spoda.ks.api.player.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EditPlayerRequest {

    private String name;
    private String alias;
    private String description;
    private String playerImg;
}

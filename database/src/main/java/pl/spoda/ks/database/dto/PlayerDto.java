package pl.spoda.ks.database.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PlayerDto {

    private Integer id;
    private String name;
    private String alias;
}

package pl.spoda.ks.database.dto;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PlayerDto {

    private Integer id;
    private String name;
    private String alias;
    private String playerImg;
    private LocalDate joinDate;
    private String desc;
}

package pl.spoda.ks.database.dto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class PlayerDto extends BaseDto {

    private Integer id;
    private String name;
    private String alias;
    private String playerImg;
    private LocalDate joinDate;
    private String desc;
}

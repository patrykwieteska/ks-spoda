package pl.spoda.ks.api.match.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class Match {

    private Integer id;
    private LocalDateTime matchTime;
    private Boolean isFinished;
    private String type;


}

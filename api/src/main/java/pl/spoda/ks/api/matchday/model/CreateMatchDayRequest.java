package pl.spoda.ks.api.matchday.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMatchDayRequest {

    private Integer seasonId;
    private LocalDate matchDayDate;
    private String location;
    private String title;
    private String headerImg;

}

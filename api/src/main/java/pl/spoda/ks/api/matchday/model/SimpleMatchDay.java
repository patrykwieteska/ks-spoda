package pl.spoda.ks.api.matchday.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMatchDay {

    private Integer id;
    private LocalDate date;
    private Integer matchDayNo;
    private Boolean isFinished;
}

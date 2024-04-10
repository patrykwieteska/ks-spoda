package pl.spoda.ks.api.matchday.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MatchDayList extends BaseResponse {

    List<SimpleMatchDay> matchDays;
}

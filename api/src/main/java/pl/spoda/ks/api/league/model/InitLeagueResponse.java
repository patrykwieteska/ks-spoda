package pl.spoda.ks.api.league.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.database.dto.LeagueDto;
import pl.spoda.ks.database.dto.MatchDayDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class InitLeagueResponse extends BaseResponse {

    private LeagueDto league;
    private List<MatchDayDto> matchDays;
}

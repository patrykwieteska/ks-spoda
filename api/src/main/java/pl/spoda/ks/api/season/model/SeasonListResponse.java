package pl.spoda.ks.api.season.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.database.dto.SeasonDto;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SeasonListResponse extends BaseResponse {

    private List<SeasonDto> seasons;
}

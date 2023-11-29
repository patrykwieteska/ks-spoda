package pl.spoda.ks.api.table.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.spoda.ks.api.commons.BaseResponse;
import pl.spoda.ks.api.season.enums.PointCountingMethod;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TableResult extends BaseResponse {

    private String header;
    private PointCountingMethod pointCountingMethod;
    private List<TableResultRow> tableRows;
}

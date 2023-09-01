package pl.spoda.ks.api.commons;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@SuperBuilder
public class DeleteResponse extends BaseResponse {

    private Integer deletedId;
}

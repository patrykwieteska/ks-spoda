package pl.spoda.ks.api.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
}

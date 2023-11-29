package pl.spoda.ks.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseDto  {

    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private String createdBy;
    private Boolean isDeleted;
}

package pl.spoda.ks.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity {

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @Column(name = "LAST_MODIFICATION_DATE")
    private LocalDateTime lastModificationDate;
    @Column(name = "CREATED_BY")
    private String createdBy;

}

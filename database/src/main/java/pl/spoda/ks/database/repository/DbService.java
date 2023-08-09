package pl.spoda.ks.database.repository;

import org.springframework.stereotype.Service;
import pl.spoda.ks.database.model.BaseEntity;

import java.time.LocalDateTime;

@Service
public class DbService {

    public static final String DEFAULT_USER = "SYSTEM";

    public void updateEntity(BaseEntity entity) {
        LocalDateTime currentDate = LocalDateTime.now();
        entity.setLastModificationDate( currentDate );
    }

    public void createEntity(BaseEntity entity) {
        LocalDateTime currentDate = LocalDateTime.now();
        entity.setCreatedBy( DEFAULT_USER );
        entity.setLastModificationDate( currentDate );
        entity.setCreationDate( currentDate );
    }
}

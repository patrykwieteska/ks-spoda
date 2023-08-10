package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.Round;

@Repository
public interface RoundRepository extends JpaRepository<Round,Integer> {

}

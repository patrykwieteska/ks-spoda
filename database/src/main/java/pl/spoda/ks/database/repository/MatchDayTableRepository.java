package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.MatchDayTable;

import java.util.List;

@Repository
public interface MatchDayTableRepository extends JpaRepository<MatchDayTable,Integer> {

    List<MatchDayTable> findByMatchDayId(Integer matchDayId);
}

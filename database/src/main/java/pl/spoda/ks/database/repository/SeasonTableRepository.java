package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.SeasonTable;

import java.util.List;

@Repository
public interface SeasonTableRepository extends JpaRepository<SeasonTable,Integer> {

    List<SeasonTable> findBySeasonId(Integer seasonId);
}

package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.MatchDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchDayRepository extends JpaRepository<MatchDay,Integer> {

    List<MatchDay> findBySeasonId(Integer seasonId);
    Optional<MatchDay> findByDate(LocalDate date);

    @Query(nativeQuery = true,value = """
            SELECT DISTINCT ID FROM MATCH_DAY WHERE SEASON_ID IN (:seasonIds);
            """)
    List<Integer> findMatchDayIdsBySeasonIds(List<Integer> seasonIds);
}


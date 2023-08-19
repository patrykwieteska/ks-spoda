package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.MatchDay;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchDayRepository extends JpaRepository<MatchDay,Integer> {

    List<MatchDay> findByLeagueId(Integer leagueId);
    Optional<MatchDay> findByDate(LocalDate date);
}
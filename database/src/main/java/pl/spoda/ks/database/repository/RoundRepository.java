package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.Round;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository extends JpaRepository<Round,Integer> {

    List<Round> findByLeagueId(Integer leagueId);
    Optional<Round> findByDate(LocalDate date);
}

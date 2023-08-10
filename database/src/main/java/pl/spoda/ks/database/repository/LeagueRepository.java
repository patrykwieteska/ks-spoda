package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.League;

import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League,Integer> {

    Optional<League> findByName(String name);
}

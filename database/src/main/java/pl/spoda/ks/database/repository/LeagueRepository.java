package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import pl.spoda.ks.database.entity.League;

import java.util.Optional;

@Component
public interface LeagueRepository extends JpaRepository<League,Integer> {

    Optional<League> findByName(String name);
}

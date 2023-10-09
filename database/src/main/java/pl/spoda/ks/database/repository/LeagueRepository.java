package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import pl.spoda.ks.database.entity.League;

import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League,Integer> {

    Optional<League> findByName(String name);
}

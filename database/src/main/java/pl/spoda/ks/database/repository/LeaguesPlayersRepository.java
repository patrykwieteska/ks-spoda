package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.LeaguesPlayers;

import java.util.List;

@Repository
public interface LeaguesPlayersRepository extends JpaRepository<LeaguesPlayers,Integer> {

    List<LeaguesPlayers> findByLeagueId(Integer leagueId);
}

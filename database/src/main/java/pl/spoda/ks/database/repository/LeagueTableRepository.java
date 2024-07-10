package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.LeagueTable;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueTableRepository extends JpaRepository<LeagueTable,Integer> {

    List<LeagueTable> findByLeagueId(Integer leagueId);
    @Query(nativeQuery = true, value = "SELECT lt.* FROM LEAGUE_TABLE lt WHERE lt.PLAYER_ID=:playerId AND lt.LEAGUE_ID=:leagueId")
    Optional<LeagueTable> findByLeagueIdAndPlayerId(Integer leagueId, Integer playerId);
}

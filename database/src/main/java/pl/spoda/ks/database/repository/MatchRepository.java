package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.Match;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {


    @Query(value = """
            SELECT * FROM MATCH_GAME M WHERE M.MATCH_DAY_ID IN (:matchDayIds);
            """, nativeQuery = true)
    List<Match> findMatchesByMatchDayIds(List<Integer> matchDayIds);


    @Query(value = """
            select m.id from match_game m where m.match_day_id =:matchDayId and m.is_finished = 0;
            """, nativeQuery = true)
    List<Integer> findUnfinishedMatchesByMatchDay(@Param("matchDayId") Integer matchDayId);


    @Query(value = """
            SELECT m.id from match_game m
                left join match_day md on m.match_day_id=md.id
                left join season s on md.season_id=s.id
                where s.league_id=:leagueId
                 order by m.id desc
                 limit 1;
            """, nativeQuery = true)
    Optional<Integer> getNewestMatch(@Param( value = "leagueId") Integer leagueId);

    @Override
    @Modifying
    @Query(value = "DELETE FROM MATCH_GAME WHERE ID = :matchId", nativeQuery = true)
    void deleteById(Integer matchId);

}

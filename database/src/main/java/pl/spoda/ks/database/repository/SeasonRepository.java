package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.Season;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season,Integer> {


    List<Season> findByLeagueId(Integer leagueId);

    @Query(nativeQuery = true,value = """
            SELECT ID FROM SEASON WHERE LEAGUE_ID =:leagueId
            
            """)
    List<Integer> findSeasonIdsByLeagueId(Integer leagueId);

}

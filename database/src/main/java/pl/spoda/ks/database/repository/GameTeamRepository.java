package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.GameTeam;

import java.util.List;

@Repository
public interface GameTeamRepository extends JpaRepository<GameTeam, Integer> {

    @Query(value = """
            select distinct game_team_id from
                      (select m.match_day_id, mt.game_team_id
                      from match_game m
            	join match_day md on md.id=m.match_day_id
            	join match_team mt where mt.id in (m.home_team_id, m.away_team_id)) t
                      where t.match_day_id =:matchDayId
            """, nativeQuery = true)
    List<Integer> getUsedGameTeam(@Param(value = "matchDayId") Integer matchDayId);

}

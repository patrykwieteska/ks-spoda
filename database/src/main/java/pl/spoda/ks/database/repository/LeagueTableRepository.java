package pl.spoda.ks.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.LeagueTable;

import java.util.List;

@Repository
public interface LeagueTableRepository extends JpaRepository<LeagueTable,Integer> {

    List<LeagueTable> findByLeagueId(Integer leagueId);
}

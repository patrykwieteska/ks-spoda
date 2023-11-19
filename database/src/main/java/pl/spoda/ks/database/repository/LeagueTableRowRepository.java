package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.table.LeagueTableRow;

import java.util.List;

@Repository
public interface LeagueTableRowRepository extends JpaRepository<LeagueTableRow,Integer> {

    List<LeagueTableRow> findByLeagueId(Integer leagueId);
}

package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.spoda.ks.database.entity.table.SeasonTableRow;

@Repository
public interface SeasonTableRowRepository extends JpaRepository<SeasonTableRow,Integer> {
}

package pl.spoda.ks.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.spoda.ks.database.entity.Player;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player,Integer> {

    Optional<Player> findByAlias(String alias);
}

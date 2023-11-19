package pl.spoda.ks.database.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.LeagueTableRowDto;
import pl.spoda.ks.database.entity.League;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.entity.Season;
import pl.spoda.ks.database.entity.table.LeagueTableRow;
import pl.spoda.ks.database.entity.table.SeasonTableRow;
import pl.spoda.ks.database.entity.table.TableBase;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.LeagueTableRowRepository;
import pl.spoda.ks.database.repository.SeasonTableRowRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TableServiceDB {

    private final LeagueTableRowRepository leagueTableRowRepository;
    private final SeasonTableRowRepository seasonTableRowRepository;
    private final BaseServiceDB baseServiceDB;
    private final EntityMapper entityMapper = Mappers.getMapper( EntityMapper.class );

    @Value("${application.initial-rating}")
    public String initialRating;

    @Transactional
    public void createTable(League league, Set<Player> playerList) {
        List<LeagueTableRow> leagueTableRows = playerList.stream()
                .map(player -> mapToLeagueTableRow(player,league))
                .toList();
        leagueTableRowRepository.saveAll( leagueTableRows );
    }

    @Transactional
    public void createTable(Season season, Set<Player> playerList) {
        List<SeasonTableRow> seasonTableRowList = playerList.stream()
                .map(player -> mapToSeasonTableRow(player,season))
                .toList();
        seasonTableRowRepository.saveAll( seasonTableRowList );
    }

    private LeagueTableRow mapToLeagueTableRow(Player player, League league) {
        LeagueTableRow leagueTableRow = mapToTableRow(player,new LeagueTableRow());
        leagueTableRow.setLeagueId( league.getId() );
        leagueTableRow.setLeague( league );
        return leagueTableRow;
    }

    private SeasonTableRow mapToSeasonTableRow(Player player, Season season) {
        SeasonTableRow seasonTableRow = mapToTableRow(player,new SeasonTableRow());
        seasonTableRow.setSeasonId(season.getId() );
        seasonTableRow.setSeason( season );
        return seasonTableRow;
    }


    private <T extends TableBase> T mapToTableRow(Player player, T tableObject) {
        tableObject.setPlayerId( player.getId() );
        tableObject.setLoses( 0 );
        tableObject.setDraws( 0);
        tableObject.setWins( 0);
        tableObject.setRating(new BigDecimal( initialRating ) );
        tableObject.setPointsTotal( 0);
        tableObject.setGoalsConceded( 0);
        tableObject.setGoalScored( 0);
        tableObject.setMatches( 0);
        baseServiceDB.createEntity( tableObject );
        return tableObject;
    }

    public List<LeagueTableRowDto> getTableByLeagueId(Integer leagueId) {
        return leagueTableRowRepository.findByLeagueId( leagueId ).stream()
                .map( entityMapper::mapToLeagueTableRowDto )
                .toList();
    }
}

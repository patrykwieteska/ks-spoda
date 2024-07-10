package pl.spoda.ks.database.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.dto.LeagueTableDto;
import pl.spoda.ks.database.entity.LeagueTable;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.LeagueTableRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeagueTableServiceDB {


    private final LeagueTableRepository leagueTableRepository;
    private final BaseServiceDB baseServiceDB;

    @Value("${application.initial-rating}")
    public String initialRating;

    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    public Set<LeagueTableDto> getCurrentLeagueTable(Integer leagueId) {
        return leagueTableRepository.findByLeagueId( leagueId ).stream()
                .map( mapper::mapToLeagueTableDto )
                .collect( Collectors.toSet() );
    }

    public void createLeagueTable(Set<Player> players, Integer leagueId) {
        List<LeagueTable> leagueTableList = players.stream()
                .map( player -> {
                    BigDecimal initialRatingValue = new BigDecimal( initialRating );
                    LeagueTable leagueTable = LeagueTable.builder()
                            .leagueId( leagueId )
                            .player( player )
                            .currentRating( initialRatingValue )
                            .previousRating( initialRatingValue )
                            .matches( BigDecimal.ZERO )
                            .pointsTotal( BigDecimal.ZERO )
                            .build();
                    baseServiceDB.createEntity( leagueTable );
                    return leagueTable;
                } )
                .toList();


        leagueTableRepository.saveAll( leagueTableList );
    }

    public boolean arePlayersInTheLeague(List<Integer> playerList, Integer leagueId) {
        return new HashSet<>( leagueTableRepository.findByLeagueId( leagueId ).stream()
                .map( row -> row.getPlayer().getId() )
                .toList() )
                .containsAll( playerList );
    }

    public void saveTable(List<LeagueTableDto> leagueTableDtoSet) {
        List<LeagueTable> leagueTableList = new ArrayList<>();
        leagueTableDtoSet.stream()
                .map( mapper::mapToLeagueTable )
                .forEach( leagueTableRow -> {
                    if (leagueTableRow.getId() != null) {
                        baseServiceDB.updateEntity( leagueTableRow );
                    } else {
                        baseServiceDB.createEntity( leagueTableRow );
                    }
                    leagueTableList.add( leagueTableRow );
                } );
        leagueTableRepository.saveAll( leagueTableList );
    }

    public List<LeagueTableDto> getLeagueTable(Integer leagueId) {
        return leagueTableRepository.findByLeagueId( leagueId ).stream()
                .map( mapper::mapToLeagueTableDto )
                .toList();
    }

    public void addPlayerToLeague(
            Player newPlayer,
            Integer leagueId
    ) {
        BigDecimal initialRatingValue = new BigDecimal( initialRating );
        LeagueTable newPlayerTable = LeagueTable.builder()
                .leagueId( leagueId )
                .player( newPlayer )
                .currentRating( initialRatingValue )
                .previousRating( initialRatingValue )
                .matches( BigDecimal.ZERO )
                .pointsTotal( BigDecimal.ZERO )
                .build();

        baseServiceDB.createEntity( newPlayerTable );
        leagueTableRepository.flush();
        leagueTableRepository.save( newPlayerTable );
    }

    public void validatePlayerInTheLeague(Integer leagueId, Integer playerId) {
        Optional<LeagueTable> playerInLeague = leagueTableRepository.findByLeagueIdAndPlayerId( leagueId, playerId );
        if (playerInLeague.isPresent()) {
            throw new SpodaApplicationException( "Gracz został już dodany do ligi" );
        }
    }
}

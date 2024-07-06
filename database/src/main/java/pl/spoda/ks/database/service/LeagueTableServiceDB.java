package pl.spoda.ks.database.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.LeagueTableDto;
import pl.spoda.ks.database.entity.LeagueTable;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.LeagueTableRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
                    LeagueTable leagueTable = LeagueTable.builder()
                            .leagueId( leagueId )
                            .currentPosition( 1 )
                            .previousPosition( 1 )
                            .player( player )
                            .currentRating( new BigDecimal( initialRating ) )
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
        List<LeagueTable> leagueTable = leagueTableRepository.findByLeagueId( leagueId );
        LeagueTable newPlayerTable = LeagueTable.builder()
                .leagueId( leagueId )
                .currentPosition( leagueTable.size()+1 )
                .previousPosition( leagueTable.size()+1 )
                .player( newPlayer )
                .currentRating( new BigDecimal( initialRating ) )
                .matches( BigDecimal.ZERO )
                .pointsTotal( BigDecimal.ZERO )
                .build();

        baseServiceDB.createEntity( newPlayerTable );
        leagueTableRepository.save( newPlayerTable );

    }
}

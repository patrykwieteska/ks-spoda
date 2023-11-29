package pl.spoda.ks.database.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.MatchDayTableDto;
import pl.spoda.ks.database.entity.MatchDayTable;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.MatchDayTableRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchDayTableServiceDB {

    private final MatchDayTableRepository matchDayTableRepository;
    private final BaseServiceDB baseServiceDB;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    public Set<MatchDayTableDto> getCurrentMatchDayTable(Integer matchDayId) {
        return matchDayTableRepository.findByMatchDayId( matchDayId )
                .stream()
                .map( mapper::mapToMatchDayTableDto )
                .collect( Collectors.toSet());
    }

    public void saveTable(List<MatchDayTableDto> matchDayTableDtoSet) {
        List<MatchDayTable> matchDayTableList = new ArrayList<>();
        matchDayTableDtoSet.stream()
                .map( mapper::mapToMatchDayTable )
                .forEach( details -> {
                    if (details.getId() != null) {
                        baseServiceDB.updateEntity( details );
                    } else {
                        baseServiceDB.createEntity( details );
                    }
                    matchDayTableList.add( details );
                } );
        matchDayTableRepository.saveAll( matchDayTableList );
    }

    public List<MatchDayTableDto> getMatchDayTable(Integer matchDayId) {
        return matchDayTableRepository.findByMatchDayId( matchDayId ).stream()
                .map( mapper::mapToMatchDayTableDto )
                .toList();
    }
}

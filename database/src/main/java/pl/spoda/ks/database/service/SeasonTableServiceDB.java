package pl.spoda.ks.database.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.SeasonTableDto;
import pl.spoda.ks.database.entity.SeasonTable;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.SeasonTableRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeasonTableServiceDB {


    private final SeasonTableRepository seasonTableRepository;
    private final BaseServiceDB baseServiceDB;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    public Set<SeasonTableDto> getCurrentSeasonTable(Integer seasonId) {
        return seasonTableRepository.findBySeasonId( seasonId ).stream()
                .map( mapper::mapToSeasonTableDto )
                .collect( Collectors.toSet() );
    }

    public void saveTable(List<SeasonTableDto> seasonPlayerDetailsDtoSet) {
        List<SeasonTable> seasonTableList = new ArrayList<>();
        seasonPlayerDetailsDtoSet.stream()
                .map( mapper::mapToSeasonTable )
                .forEach( details -> {
                    if (details.getId() != null) {
                        baseServiceDB.updateEntity( details );
                    } else {
                        baseServiceDB.createEntity( details );
                    }
                    seasonTableList.add( details );
                } );
        seasonTableRepository.saveAll( seasonTableList );
    }

    public List<SeasonTableDto> getSeasonTable(Integer seasonId) {
        return seasonTableRepository.findBySeasonId( seasonId ).stream()
                .map( mapper::mapToSeasonTableDto )
                .toList();
    }
}

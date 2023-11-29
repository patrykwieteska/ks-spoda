package pl.spoda.ks.database.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.dto.GameTeamDto;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.GameTeamRepository;

@Service
@RequiredArgsConstructor
public class GameTeamServiceDB {


    private final GameTeamRepository gameTeamRepository;
    private final EntityMapper mapper = Mappers.getMapper( EntityMapper.class );

    public GameTeamDto getGameTeamById(Integer gameTeamId) {

        return mapper.mapToGameTeamDto( gameTeamRepository
                .findById( gameTeamId )
                .orElseThrow(
                        () -> new SpodaApplicationException( "Nie znaleziono drużyny o id=" + gameTeamId ) )
        );
    }
}

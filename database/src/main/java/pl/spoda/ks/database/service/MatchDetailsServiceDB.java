package pl.spoda.ks.database.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.MatchDetailsDto;
import pl.spoda.ks.database.entity.MatchDetails;
import pl.spoda.ks.database.mapper.EntityMapper;
import pl.spoda.ks.database.repository.MatchDetailsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchDetailsServiceDB {

    private final EntityMapper entityMapper = Mappers.getMapper( EntityMapper.class );
    private final MatchDetailsRepository matchDetailsRepository;
    private final BaseServiceDB baseServiceDB;


    public MatchDetailsDto findLatestMatchDetails(Integer playerId, Integer leagueId) {
        Integer newestMatchDetailsId = matchDetailsRepository.findNewestPlayerMatchInLeague(playerId,leagueId);
        if(newestMatchDetailsId == null) {
            return null;
        }

        return matchDetailsRepository
                .findById( newestMatchDetailsId)
                .map( entityMapper::mapToMatchDetailsDto )
                .orElse( null );
    }

    public List<MatchDetailsDto> findMatchDetailsList(Integer matchId) {
        return matchDetailsRepository.findByMatchId( matchId ).stream()
                .map( entityMapper::mapToMatchDetailsDto )
                .toList();
    }

    public void saveDetails(List<MatchDetailsDto> matchDetailsDtoList, Integer matchId) {
        List<MatchDetails> matchDetailsEntities = new ArrayList<>();
        matchDetailsDtoList.stream().map( entityMapper::mapToMatchDetails )
                .forEach( details -> {
                    if (details.getId() != null) {
                        baseServiceDB.updateEntity( details );
                    } else {
                        baseServiceDB.createEntity( details );
                    }
                    details.setMatchId( matchId );
                    matchDetailsEntities.add( details );
                });
        matchDetailsRepository.saveAll( matchDetailsEntities );
    }
}

package pl.spoda.ks.database.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;
import pl.spoda.ks.database.dto.MatchDto;
import pl.spoda.ks.database.repository.MatchRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceDB {

    private MatchRepository matchRepository;

    public List<MatchDto> findMatchesByLeague(Integer leagueId) {
        throw new SpodaApplicationException( "Not implemented yed" );

    }
}

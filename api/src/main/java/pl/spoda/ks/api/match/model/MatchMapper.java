package pl.spoda.ks.api.match.model;

import org.springframework.stereotype.Service;
import pl.spoda.ks.database.dto.MatchDto;

@Service
public class MatchMapper {

    public Match mapToResponse(MatchDto matchDto) {
        return Match.builder()
                .id(matchDto.getId())
                .type( matchDto.getType() )
                .isFinished( matchDto.getIsFinished() )
                .matchTime( matchDto.getMatchTime() )
                .build();

    }
}

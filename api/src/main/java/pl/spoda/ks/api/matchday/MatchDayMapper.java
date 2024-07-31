package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.api.matchday.model.SimpleMatchDay;
import pl.spoda.ks.comons.date.DateService;
import pl.spoda.ks.database.dto.MatchDayDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchDayMapper {

    private final DateService dateService;

    public MatchDayDto mapMatchDay(CreateMatchDayRequest request) {
        return MatchDayDto.builder()
                .date( dateService.dateOf( request.getMatchDayDate() ) )
                .seasonId( request.getSeasonId() )
                .location( request.getLocation() )
                .title( request.getTitle() )
                .headerImg( request.getHeaderImg() )
                .build();
    }

    public List<SimpleMatchDay> mapToSimpleMatchDayList(List<MatchDayDto> matchDaysBySeasonId) {
        return matchDaysBySeasonId.stream()
                .map( this::mapToSimpleMatchDay )
                .toList();
    }

    private SimpleMatchDay mapToSimpleMatchDay(MatchDayDto matchDayDto) {
        return SimpleMatchDay.builder()
                .id( matchDayDto.getId() )
                .date( matchDayDto.getDate() )
                .matchDayNo( matchDayDto.getSeasonMatchDay() )
                .isFinished( matchDayDto.getIsFinished() )
                .build();
    }
}

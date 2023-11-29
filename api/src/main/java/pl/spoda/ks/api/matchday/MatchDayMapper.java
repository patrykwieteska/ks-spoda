package pl.spoda.ks.api.matchday;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.spoda.ks.api.match.model.MatchMapper;
import pl.spoda.ks.api.matchday.model.CreateMatchDayRequest;
import pl.spoda.ks.api.matchday.model.init.MatchDayData;
import pl.spoda.ks.comons.date.DateService;
import pl.spoda.ks.database.dto.MatchDayDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchDayMapper {

    private final DateService dateService;
    private final MatchMapper matchMapper;

    public MatchDayDto mapMatchDay(CreateMatchDayRequest request) {
        return MatchDayDto.builder()
                .date( dateService.dateOf( request.getMatchDayDate() ) )
                .seasonId( request.getSeasonId() )
                .location( request.getLocation() )
                .build();
    }

    public List<MatchDayData> mapToSeasonMatchDayList(List<MatchDayDto> matchDayDtoList) {
        return matchDayDtoList.stream()
                .map( this::mapToMatchDayData )
                .toList();

    }

    private MatchDayData mapToMatchDayData(MatchDayDto matchDayDto) {
        return MatchDayData.builder()
                .matches( matchMapper.mapToMatchList( matchDayDto.getMatchList() ) )
                .matchDayId( matchDayDto.getId() )
                .seasonMatchDay( matchDayDto.getSeasonMatchDay() )
                .isFinished( matchDayDto.getIsFinished() )
                .location( matchDayDto.getLocation() )
                .date( matchDayDto.getDate() )
                .build();

    }

}

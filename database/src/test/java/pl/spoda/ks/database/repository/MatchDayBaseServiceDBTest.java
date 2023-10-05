package pl.spoda.ks.database.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.spoda.ks.database.service.MatchDayServiceDB;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.entity.MatchDay;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchDayBaseServiceDBTest {


    private static final LocalDate MATCH_DAY_1_DATE = LocalDate.of( 2021, 3, 3 );
    private static final LocalDate MATCH_DAY_2_DATE = LocalDate.of( 2022, 2, 2 );
    private static final LocalDate MATCH_DAY_3_DATE = LocalDate.of( 2023, 1, 1 );
    @Mock
    private MatchDayRepository matchDayRepository;
    @InjectMocks
    private MatchDayServiceDB matchDayServiceDb;

    private static final Integer SEASON_ID = 1;

    @Test
    void getMatchDaysByLeagueId_test() {
        when( matchDayRepository.findBySeasonId( SEASON_ID ) ).thenReturn( stubMatchDays() );
        List<MatchDayDto> result = matchDayServiceDb.getMatchDaysBySeasonId( SEASON_ID );
        Assertions.assertThat( result ).containsExactly(
                MatchDayDto.builder()
                        .date( MATCH_DAY_3_DATE )
                        .seasonMatchDay( 3 )
                        .seasonId( SEASON_ID )
                        .build(),
                MatchDayDto.builder()
                        .date( MATCH_DAY_2_DATE )
                        .seasonMatchDay( 2 )
                        .seasonId( SEASON_ID )
                        .build(),
                MatchDayDto.builder()
                        .date( MATCH_DAY_1_DATE )
                        .seasonMatchDay( 1 )
                        .seasonId( SEASON_ID )
                        .build()
        );
    }

    private List<MatchDay> stubMatchDays() {
        return List.of(
                MatchDay.builder()
                        .date( MATCH_DAY_1_DATE )
                        .seasonId( SEASON_ID )
                        .seasonMatchDay( 1 )
                        .build(),
                MatchDay.builder()
                        .date( MATCH_DAY_3_DATE )
                        .seasonId( SEASON_ID )
                        .seasonMatchDay( 3 )
                        .build(),
                MatchDay.builder()
                        .date( MATCH_DAY_2_DATE )
                        .seasonId( SEASON_ID )
                        .seasonMatchDay( 2 )
                        .build()
        );
    }


}
package pl.spoda.ks.database.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.spoda.ks.database.dto.MatchDayDto;
import pl.spoda.ks.database.entity.MatchDay;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchDayServiceDbTest {


    private static final LocalDate ROUND_1_DATE = LocalDate.of( 2021, 3, 3 );
    private static final LocalDate ROUND_2_DATE = LocalDate.of( 2022, 2, 2 );
    private static final LocalDate ROUND_3_DATE = LocalDate.of( 2023, 1, 1 );
    @Mock
    private DbService dbService;
    @Mock
    private MatchDayRepository matchDayRepository;
    @InjectMocks
    private MatchDayServiceDb matchDayServiceDb;

    private static final Integer LEAGUE_ID = 1;

    @Test
    void getMatchDaysByLeagueId_test() {
        when( matchDayRepository.findByLeagueId( LEAGUE_ID ) ).thenReturn( stubMatchDays() );
        List<MatchDayDto> result = matchDayServiceDb.getMatchDaysByLeagueId( LEAGUE_ID );
        Assertions.assertThat( result ).containsExactly(
                MatchDayDto.builder()
                        .date( ROUND_3_DATE )
                        .leagueMatchDayNumber( 3 )
                        .leagueId( LEAGUE_ID )
                        .build(),
                MatchDayDto.builder()
                        .date( ROUND_2_DATE )
                        .leagueMatchDayNumber( 2 )
                        .leagueId( LEAGUE_ID )
                        .build(),
                MatchDayDto.builder()
                        .date( ROUND_1_DATE )
                        .leagueMatchDayNumber( 1 )
                        .leagueId( LEAGUE_ID )
                        .build()
        );
    }

    private List<MatchDay> stubMatchDays() {
        return List.of(
                MatchDay.builder()
                        .date( ROUND_1_DATE )
                        .leagueId( LEAGUE_ID )
                        .build(),
                MatchDay.builder()
                        .date( ROUND_3_DATE )
                        .leagueId( LEAGUE_ID )
                        .build(),
                MatchDay.builder()
                        .date( ROUND_2_DATE )
                        .leagueId( LEAGUE_ID )
                        .build()
        );
    }


}
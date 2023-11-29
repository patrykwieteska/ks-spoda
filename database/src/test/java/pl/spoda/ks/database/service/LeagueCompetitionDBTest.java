package pl.spoda.ks.database.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.spoda.ks.database.entity.LeagueTable;
import pl.spoda.ks.database.entity.Player;
import pl.spoda.ks.database.repository.LeagueTableRepository;

import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class LeagueCompetitionDBTest {

    @Spy
    private LeagueTableRepository leagueTableRepository;
    @InjectMocks
    private LeagueTableServiceDB leagueTableServiceDB;

    private static final Integer LEAGUE_ID = 1;

    public static Stream<Arguments> testParams() {
        return Stream.of(
                Arguments.of( List.of( 1, 2, 3 ), true ),
                Arguments.of( List.of( 1, 2 ), true ),
                Arguments.of( List.of( 1, 3,4), false )
        );
    }

    @ParameterizedTest
    @MethodSource(value = "testParams")
    void arePlayersInTheLeague(
            List<Integer> playerList,
            boolean expectedResult
    ) {

        Mockito.when( leagueTableRepository.findByLeagueId( LEAGUE_ID ) ).thenReturn( stubLeagueTable() );

        boolean result = leagueTableServiceDB.arePlayersInTheLeague(playerList,LEAGUE_ID);

        Assertions.assertThat(result).isEqualTo( expectedResult );

    }

    private List<LeagueTable> stubLeagueTable() {
        return List.of(
                LeagueTable.builder()
                        .player( Player.builder().id( 1 ).build() )
                        .build(),
                LeagueTable.builder()
                        .player( Player.builder().id( 2 ).build() )
                        .build(),
                LeagueTable.builder()
                        .player( Player.builder().id( 3 ).build() )
                        .build()
        );
    }
}
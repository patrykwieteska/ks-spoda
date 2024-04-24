package pl.spoda.ks.api.match;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.spoda.ks.api.euro.EuroMatchSchedule;
import pl.spoda.ks.euro.EuroService;
import pl.spoda.ks.euro.model.EuroMatch;
import pl.spoda.ks.euro.model.response.EuroCalendarResponse;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class EuroMatchServiceTest {


    public static final String GROUP = null;
    @Mock
    private EuroService euroService;

    @InjectMocks
    private EuroMatchService euroMatchService;


    @Test
    void getNotPlayedMatches() {
        Mockito.when( euroService.getEuroCalendar( GROUP ) ).thenReturn( stubResponse() );
        EuroMatchSchedule result = euroMatchService.getNotPlayedMatches();

        Assertions.assertThat( result ).isEqualTo(
                EuroMatchSchedule.builder()
                        .matches( List.of(
                                EuroMatch.builder()
                                        .matchNumber( 11 )
                                        .played( false )
                                        .build(),
                                EuroMatch.builder()
                                        .matchNumber( 27 )
                                        .played( false )
                                        .build(),
                                EuroMatch.builder()
                                        .matchNumber( 33 )
                                        .played( false )
                                        .build()
                        ) )
                        .build()
        );
    }

    @Test
    void getPlayedMatches() {
        Mockito.when( euroService.getEuroCalendar( GROUP ) ).thenReturn( stubResponse() );
        EuroMatchSchedule result = euroMatchService.getPlayedMatches( GROUP, 4 );
        Assertions.assertThat( result ).isEqualTo(
                EuroMatchSchedule.builder()
                        .matches( List.of(
                                EuroMatch.builder()
                                        .matchNumber( 5 )
                                        .played( true )
                                        .build(),
                                EuroMatch.builder()
                                        .matchNumber( 77 )
                                        .played( true )
                                        .build()
                        ) )
                        .build()
        );
    }

    private EuroCalendarResponse stubResponse() {
        return EuroCalendarResponse.builder()
                .euroMatches( List.of(
                        EuroMatch.builder()
                                .matchNumber( 77 )
                                .played( true )
                                .build(),
                        EuroMatch.builder()
                                .matchNumber( 27 )
                                .played( false )
                                .build(),
                        EuroMatch.builder()
                                .matchNumber( 5 )
                                .played( true )
                                .build(),
                        EuroMatch.builder()
                                .matchNumber( 33 )
                                .played( false )
                                .build(),
                        EuroMatch.builder()
                                .matchNumber( 11 )
                                .played( false )
                                .build()
                ) )
                .build();
    }
}
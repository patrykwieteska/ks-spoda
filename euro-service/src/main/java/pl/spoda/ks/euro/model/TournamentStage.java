package pl.spoda.ks.euro.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TournamentStage {

    GROUP( "Grupa" ,1),
    ROUND_OF_16( "1/8 finału" ,2),
    QUARTER_FINALS( "Ćwierćfinał",3 ),
    SEMI_FINALS( "Półfinał",4 ),
    FINAL( "Finał" ,5);

    @Getter
    private final String description;
    private final int order;


}

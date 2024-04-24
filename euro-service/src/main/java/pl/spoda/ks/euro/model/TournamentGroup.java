package pl.spoda.ks.euro.model;

import java.util.Arrays;

public enum TournamentGroup {

    ALL,A, B, C, D, E, F, PLAYOFF;


    public static TournamentGroup getByName(String name) {
        return Arrays.stream( values() )
                .filter( x -> x.name().equals( name ) )
                .findFirst()
                .orElse( ALL );
    }

}

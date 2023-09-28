package pl.spoda.ks.api.league.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum LeagueType {

    SEASON,
    CUP;

    private static final Map<String, LeagueType> LEAGUE_TYPE_MAP = Arrays.stream( LeagueType.values() )
            .collect( Collectors.toMap(
                    Enum::name,
                    enumValue -> enumValue ) );

    public static LeagueType getByName(String name) {
        return LEAGUE_TYPE_MAP.get( name );
    }
}
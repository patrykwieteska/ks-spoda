package pl.spoda.ks.api.league.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TeamStructure {

    SINGLE,
    DOUBLE,
    MIXED;

    private static final Map<String, TeamStructure> TEAM_STRUCTURE_MAP = Arrays.stream( TeamStructure.values() )
            .collect( Collectors.toMap(
                    Enum::name,
                    enumValue -> enumValue ) );

    public static TeamStructure getByName(String name) {
        return TEAM_STRUCTURE_MAP.get( name );
    }
}

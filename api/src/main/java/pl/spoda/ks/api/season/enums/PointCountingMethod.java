package pl.spoda.ks.api.season.enums;

import java.util.Arrays;

public enum PointCountingMethod {

    RATING,
    POINTS_TOTAL,
    POINTS_PER_MATCH;

    public static PointCountingMethod getByName(String pointCountingMethod) {
        return Arrays.stream( values() )
              .filter( value -> value.name().equals( pointCountingMethod ) )
              .findFirst()
              .orElse( null);
    }
}
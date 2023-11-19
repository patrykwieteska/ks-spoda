package pl.spoda.ks.api.season.enums;



import java.util.Arrays;

public enum RatingType {
    TEAM,
    SINGLE;

    public static RatingType getByName(String ratingType) {
        return Arrays.stream( values() )
                .filter( value -> value.name().equals( ratingType ) )
                .findFirst()
                .orElse(null);
    }
}
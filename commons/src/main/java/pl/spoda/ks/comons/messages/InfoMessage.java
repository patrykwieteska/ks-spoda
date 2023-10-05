package pl.spoda.ks.comons.messages;

public class InfoMessage {

    public static final String MATCHDAYS_NOT_FINISHED = "Aby zakończyć sezon należy w pierwszej kolejności zakończyć wszystkie kolejki";
    public static final String UNFINISHED_SEASONS_IN_LEAGUE = "Istnieją niezakończone sezony w lidze";
    public static final String ALIAS_ALREADY_EXISTS = "Player with alias '%s' already exists";
    public static final String NO_PLAYERS_FOUND = "Nie znaleziono graczy";

    private InfoMessage() {
        throw new IllegalStateException( "Utility class" );
    }

    public static final String LEAGUE_NAME_ALREADY_TAKEN = "Nazwa ligi jest już zajęta";
    public static final String NO_SEASONS_FOUND = "Nie znaleziono lig";
    public static final String LEAGUE_NOT_FOUND = "Nie znaleziono ligi o podanym id";
    public static final String SEASON_NOT_FOUND = "Nie znaleziono sezonu o podanym id";

    public static String getMessage(String message, String value) {
        return String.format( "%s: %s", message, value );
    }
}

package pl.spoda.ks.comons.messages;

public class InfoMessage {

    public static final String MATCHDAYS_NOT_FINISHED = "Aby zakończyć sezon należy w pierwszej kolejności zakończyć " +
            "wszystkie kolejki w danym sezonie";
    public static final String UNFINISHED_SEASONS_IN_LEAGUE = "Istnieją niezakończone sezony w lidze";
    public static final String ALIAS_ALREADY_EXISTS = "Player with alias '%s' already exists";
    public static final String NO_PLAYERS_FOUND = "Nie znaleziono graczy";
    public static final String RATING_TYPE_REQUIRED = "Należy wybrać typ ratingu";
    public static final String NOT_IMPLEMENTED_YET = "Nie zaimplementowano funkcjonalności";
    public static final String PLAYER_HAS_MATCH_IN_PROGRESS = "Przynajmniej jeden z graczy aktualnie rozgrywa mecz.\nZakończ mecze graczy przed tworzeniem kolejnych";
    public static final String NO_PLAYER_IN_THE_LEAGUE = "W meczu znajdują się gracze nienależący do tej ligi";
    public static final String MATCH_DAY_FINISHED = "Brak możliwości dodania meczu. Zakończono kolejkę: ";
    public static final String MATCH_FINISHED = "Brak możliwości edycji zakończonego meczu ";
    public static final String DUPLICATE_PLAYERS_IN_MATCH_REQUEST = "W meczu znajdują się duplikaty graczy";
    public static final String NOT_FINISHED_MATCHES = "Istnieją niezakończone mecze w kolejce. Zakończ mecze przed " +
            "zakończeniem kolejki";
    public static final String REMOVE_NEWEST_MATCHES = "Istnieją nowsze mecze w tej lidze. Możliwe jest usunięcie " +
            "jedynie najnowszego meczu w danej lidze";
    public static final String INVALID_TEAM_STRUCTURE = "Dla wybranej struktury drużyn liczba graczy w drużynie musi " +
            "być równa";
    public static final String UNFINISHED_MATCH_DAYS = "Istnieją niezakończone kolejki w sezonie";

    private InfoMessage() {
        throw new IllegalStateException( "Utility class" );
    }

    public static final String LEAGUE_NAME_ALREADY_TAKEN = "Nazwa ligi jest już zajęta";
    public static final String NO_SEASONS_FOUND = "Nie znaleziono sezonów";
    public static final String LEAGUE_NOT_FOUND = "Nie znaleziono ligi o podanym id";
    public static final String SEASON_NOT_FOUND = "Nie znaleziono sezonu o podanym id";

    public static String getMessage(String message, String value) {
        return String.format( "%s: %s", message, value );
    }
}

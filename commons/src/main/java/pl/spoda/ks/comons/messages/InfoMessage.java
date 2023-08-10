package pl.spoda.ks.comons.messages;

public class InfoMessage {

    private InfoMessage() {
        throw new IllegalStateException("Utility class");
    }
    public static final String LEAGUE_NAME_ALREADY_TAKEN = "Nazwa ligi jest już zajęta";
    public static final String NO_LEAGUES_FOUND = "Nie znaleziono lig";
    public static final String LEAGUE_ID_REQUIRED = "Brak leagueId w żądaniu";
    public static final String LEAGUE_NOT_FOUND = "Nie znaleziono ligi o podanym id";

    public static String getMessage(String message, String value) {
        return String.format( "%s: %s",message,value );
    }
}

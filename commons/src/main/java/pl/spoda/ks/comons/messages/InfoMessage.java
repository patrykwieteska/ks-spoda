package pl.spoda.ks.comons.messages;

public class InfoMessage {

    private InfoMessage() {
        throw new IllegalStateException("Utility class");
    }

    public static final String LEAGUE_NAME_ALREADY_TAKEN = "Nazwa ligi jest już zajęta";
    public static final String NO_LEAGUES_FOUND = "Nie znaleziono lig";
}

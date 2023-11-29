package pl.spoda.ks.api.table.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.spoda.ks.comons.exceptions.SpodaApplicationException;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum PreviousPositionReference {
    
    UP("up"),
    DOWN("down"),
    NONE("none");

    private final String value;

}

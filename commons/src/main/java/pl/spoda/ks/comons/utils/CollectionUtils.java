package pl.spoda.ks.comons.utils;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
        return collection.isEmpty()
                ? new ArrayList<>()
                : collection;
    }
}

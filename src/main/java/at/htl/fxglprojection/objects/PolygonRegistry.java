package at.htl.fxglprojection.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolygonRegistry {
    private final static List<Polygon3D> polygons = new ArrayList<>();
    private static boolean changedSinceRead = true;

    public static void register(Polygon3D p) throws DuplicatePolygonException {
        changedSinceRead = true;

        if (p == null)
            throw new NullPointerException("Attempted to register null polygon");

        if (!polygons.contains(p)) {
            polygons.add(p);
        } else {
            throw new DuplicatePolygonException(p);
        }
    }

    public static boolean unregister(Polygon3D p) {
        changedSinceRead = true;

        return polygons.remove(p);
    }

    public static List<Polygon3D> getRegistered() {
        changedSinceRead = false;

        // https://www.baeldung.com/java-immutable-list
        return Collections.unmodifiableList(polygons);
    }

    public static boolean changedSinceRead() {
        return changedSinceRead;
    }
}

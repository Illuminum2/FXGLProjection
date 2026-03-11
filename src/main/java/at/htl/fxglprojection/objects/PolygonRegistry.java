package at.htl.fxglprojection.objects;

import java.util.ArrayList;
import java.util.List;

public class PolygonRegistry {
    private final static List<Polygon> polygons = new ArrayList<>();
    private static boolean changedSinceRead = true;

    public static void register(Polygon p) throws DuplicatePolygonException {
        changedSinceRead = true;

        if (p == null)
            throw new NullPointerException("Attempted to register null polygon");

        if (!polygons.contains(p)) {
            polygons.add(p);
        } else {
            throw new DuplicatePolygonException(p);
        }
    }

    public static boolean unregister(Polygon p) {
        changedSinceRead = true;

        return polygons.remove(p);
    }

    public static List<Polygon> getRegistered() {
        changedSinceRead = false;
        return polygons;
    }

    public static boolean changedSinceRead() {
        return changedSinceRead;
    }
}

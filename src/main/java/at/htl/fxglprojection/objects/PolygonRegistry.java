package at.htl.fxglprojection.objects;

import java.util.ArrayList;
import java.util.List;

public class PolygonRegistry {
    private final static List<Polygon3DComponent> polygons = new ArrayList<>();
    private static boolean changedSinceRead = true;

    public static void register(Polygon3DComponent p) throws DuplicatePolygonException {
        changedSinceRead = true;

        if (p == null)
            throw new NullPointerException("Attempted to register null polygon");

        if (!polygons.contains(p)) {
            polygons.add(p);
        } else {
            throw new DuplicatePolygonException(p);
        }
    }

    public static boolean unregister(Polygon3DComponent p) {
        changedSinceRead = true;

        return polygons.remove(p);
    }

    public static List<Polygon3DComponent> getRegistered() {
        changedSinceRead = false;
        return polygons;
    }

    public static boolean changedSinceRead() {
        return changedSinceRead;
    }
}

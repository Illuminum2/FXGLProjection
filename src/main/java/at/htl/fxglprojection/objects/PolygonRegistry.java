package at.htl.fxglprojection.objects;

import java.util.ArrayList;
import java.util.List;

public class PolygonRegistry {
    private final static List<Polygon> polygons = new ArrayList<>();
    private static boolean hasChangedSinceRead = true;

    public static void registerPolygon(Polygon p) throws DuplicatePolygonException {
        hasChangedSinceRead = true;

        if (!polygons.contains(p)) {
            polygons.add(p);
        } else {
            throw new DuplicatePolygonException(p);
        }
    }

    public static boolean unregisterPolygon(Polygon p) {
        hasChangedSinceRead = true;

        if (polygons.contains(p)) {
            polygons.remove(p);
            return true;
        } else {
            return false;
        }
    }

    public static List<Polygon> getRegistered() {
        hasChangedSinceRead = false;
        return polygons;
    }

    public static boolean isHasChangedSinceRead() {
        return hasChangedSinceRead;
    }
}

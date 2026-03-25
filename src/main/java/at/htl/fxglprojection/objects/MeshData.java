package at.htl.fxglprojection.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeshData {
    private final List<Polygon3D> polygons = new ArrayList<>();
    private boolean changedSinceRead = true;

    public void register(Polygon3D p) throws DuplicatePolygonException {
        changedSinceRead = true;

        if (p == null)
            throw new NullPointerException("Attempted to register null polygon");

        if (!polygons.contains(p)) {
            polygons.add(p);
        } else {
            throw new DuplicatePolygonException(p);
        }
    }

    public void register(List<Polygon3D> polygons) throws DuplicatePolygonException {
        for (Polygon3D polygon : polygons)
            register(polygon);
    }

    public boolean unregister(Polygon3D p) {
        changedSinceRead = true;

        return polygons.remove(p);
    }

    public void unregister(List<Polygon3D> polygons) {
        for (Polygon3D polygon : polygons)
            unregister(polygon);
    }

    public List<Polygon3D> getRegistered() {
        changedSinceRead = false;

        // https://www.baeldung.com/java-immutable-list
        return Collections.unmodifiableList(polygons);
    }

    public boolean changedSinceRead() {
        return changedSinceRead;
    }
}

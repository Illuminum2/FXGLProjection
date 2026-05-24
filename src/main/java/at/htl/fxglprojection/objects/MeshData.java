package at.htl.fxglprojection.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeshData {
    private final List<Polygon3D> polygons = new ArrayList<>();
    private boolean changedSinceRead = true;
    private boolean immutable = false;

    public void register(Polygon3D p) throws DuplicatePolygonException {
        checkMutable();

        changedSinceRead = true;

        if (p == null)
            throw new NullPointerException("Attempted to register null polygon");

        if (!polygons.contains(p)) {
            polygons.add(p);
        } else {
            throw new DuplicatePolygonException(p.toString());
        }
    }

    public void register(List<Polygon3D> polygons) throws DuplicatePolygonException {
        for (Polygon3D p : polygons)
            register(p);
    }

    public boolean unregister(Polygon3D p) {
        checkMutable();

        changedSinceRead = true;

        return polygons.remove(p);
    }

    public void unregister(List<Polygon3D> polygons) {
        for (Polygon3D p : polygons)
            unregister(p);
    }

    public List<Polygon3D> getRegistered() {
        changedSinceRead = false;

        // https://www.baeldung.com/java-immutable-list
        return Collections.unmodifiableList(polygons);
    }

    public boolean changedSinceRead() {
        return !immutable && changedSinceRead;
    }

    public MeshData mutableCopy() {
        MeshData copy = new MeshData();

        try {
            copy.register(getRegistered());
        } catch (DuplicatePolygonException _) {} // Exception is impossible

        return copy;
    }

    public boolean isImmutable() {
        return immutable;
    }

    void makeImmutable() {
        immutable = true;
        changedSinceRead = false;

        for (Polygon3D p : polygons)
            p.makeImmutable();
    }

    private void checkMutable() {
        if (immutable)
            throw new UnsupportedOperationException("Mesh data is immutable.");
    }
}

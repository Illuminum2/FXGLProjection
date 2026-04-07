package at.htl.fxglprojection.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectRegistry {
    private static final List<Mesh3DComponent> meshComponents = new ArrayList<>();
    private static boolean changedSinceRead = true;

    public static void register(Mesh3DComponent m) throws DuplicateMeshException {
        changedSinceRead = true;

        if (m == null)
            throw new NullPointerException("Attempted to register null mesh component");

        if (!meshComponents.contains(m)) {
            meshComponents.add(m);
        } else {
            throw new DuplicateMeshException(m.toString());
        }
    }

    public static void register(List<Mesh3DComponent> mesh3Dcomponents) throws DuplicateMeshException {
        for (Mesh3DComponent m : mesh3Dcomponents)
            register(m);
    }

    public static boolean unregister(Mesh3DComponent m) {
        changedSinceRead = true;

        return meshComponents.remove(m);
    }

    public static void unregister(List<Mesh3DComponent> mesh3Dcomponents) {
        for (Mesh3DComponent m : mesh3Dcomponents)
            unregister(m);
    }

    public static List<Mesh3DComponent> getRegistered() {
        changedSinceRead = false;

        // https://www.baeldung.com/java-immutable-list
        return Collections.unmodifiableList(meshComponents);
    }

    public static boolean changedSinceRead() {
        return changedSinceRead;
    }
}
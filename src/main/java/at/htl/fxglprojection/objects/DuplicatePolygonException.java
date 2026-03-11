package at.htl.fxglprojection.objects;

public class DuplicatePolygonException extends Exception {
    private final Polygon3DComponent p;

    public DuplicatePolygonException(Polygon3DComponent p) {
        super("Polygon " + p.toString() + " is already registered");
        this.p = p;
    }

    public Polygon3DComponent getPolygon() {
        return p;
    }
}

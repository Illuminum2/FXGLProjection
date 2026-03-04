package at.htl.fxglprojection.objects;

public class DuplicatePolygonException extends Exception {
    private final Polygon p;

    public DuplicatePolygonException(Polygon p) {
        super("Polygon " + p.toString() + " is already registered.");
        this.p = p;
    }

    public Polygon getPolygon() {
        return p;
    }
}

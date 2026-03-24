package at.htl.fxglprojection.objects;

public class DuplicatePolygonException extends Exception {
    private final Polygon3D p;

    public DuplicatePolygonException(Polygon3D p) {
        super("Polygon " + p.toString() + " is already registered");
        this.p = p;
    }

    public Polygon3D getPolygon() {
        return p;
    }
}

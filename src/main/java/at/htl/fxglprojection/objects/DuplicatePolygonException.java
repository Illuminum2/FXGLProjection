package at.htl.fxglprojection.objects;

public class DuplicatePolygonException extends Exception {
    public DuplicatePolygonException(String polygon) {
        super("Polygon " + polygon + " is already registered.");
    }
}
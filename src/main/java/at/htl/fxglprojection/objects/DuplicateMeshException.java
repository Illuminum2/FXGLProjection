package at.htl.fxglprojection.objects;

public class DuplicateMeshException extends RuntimeException {
    public DuplicateMeshException(String mesh) {
        super("Mesh " + mesh + " is already registered.");
    }
}

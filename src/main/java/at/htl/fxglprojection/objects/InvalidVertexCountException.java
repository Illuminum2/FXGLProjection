package at.htl.fxglprojection.objects;

public class InvalidVertexCountException extends RuntimeException {
    public InvalidVertexCountException(int count, int expected) {
        super("Expected " + expected + " vertices, got " + count);
    }
}

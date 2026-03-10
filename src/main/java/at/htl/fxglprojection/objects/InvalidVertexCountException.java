package at.htl.fxglprojection.objects;

public class InvalidVertexCountException extends RuntimeException {
    public InvalidVertexCountException(Class<?> shapeClass, int count, int expected) {
        super(shapeClass.getName() + " expected " + expected + " vertices, got " + count);
    }
}

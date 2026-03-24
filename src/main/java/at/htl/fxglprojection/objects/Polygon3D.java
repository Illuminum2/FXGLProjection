package at.htl.fxglprojection.objects;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import at.htl.fxglprojection.projection.Vec3D;

public class Polygon3D implements Vertices {
    private List<Vec3D> points;

    // https://github.com/AlmasB/FXGLGames/blob/master/Breakout/src/main/java/com/almasb/fxglgames/breakout/components/BallComponent.java
    private ObjectProperty<Color> fillColor = new SimpleObjectProperty<>(Color.GREY);

    public Polygon3D(List<Vec3D> points) {
        this.points = points;

        try {
            PolygonRegistry.register(this);
        } catch (DuplicatePolygonException e) {
            throw new RuntimeException(e);
        }
    }

    public Polygon3D(List<Vec3D> points, Color fillColor) {
        this(points);
        setFillColor(fillColor);
    }

    public Polygon3D(List<Vec3D> points, int expectedVertexCount) {
        this(validate(points, expectedVertexCount));
    }

    public Polygon3D(List<Vec3D> points, int expectedVertexCount, Color fillColor) {
        this(validate(points, expectedVertexCount), fillColor);
    }

    private static List<Vec3D> validate(List<Vec3D> points, int expected) {
        if (points.size() != expected) {
            throw new InvalidVertexCountException(points.size(), expected);
        }
        return points;
    }

    public void unregister() { PolygonRegistry.unregister(this); }

    public List<Vec3D> getVertices() {
        return points;
    }

    public Color getFillColor() {
        return this.fillColor.get();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor.set(fillColor);
    }
}

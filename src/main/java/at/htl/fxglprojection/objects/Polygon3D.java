package at.htl.fxglprojection.objects;

import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import at.htl.fxglprojection.projection.Vec3D;

public class Polygon3D implements Vertices {
    private List<Vec3D> points;
    private Vec3D normal;

    // https://github.com/AlmasB/FXGLGames/blob/master/Breakout/src/main/java/com/almasb/fxglgames/breakout/components/BallComponent.java
    private ObjectProperty<Color> fillColor = new SimpleObjectProperty<>(Color.GREY);

    public Polygon3D(List<Vec3D> points, Vec3D normal) {
        this.points = points;
        this.normal = normal;
    }

    public Polygon3D(List<Vec3D> points, Vec3D normal, Color fillColor) {
        this(points, normal);
        setFillColor(fillColor);
    }

    public Polygon3D(List<Vec3D> points, int expectedVertexCount, Vec3D normal) {
        this(validate(points, expectedVertexCount), normal);
    }

    public Polygon3D(List<Vec3D> points, int expectedVertexCount, Vec3D normal, Color fillColor) {
        this(validate(points, expectedVertexCount), normal, fillColor);
    }

    private static List<Vec3D> validate(List<Vec3D> points, int expected) {
        if (points.size() != expected) {
            throw new InvalidVertexCountException(points.size(), expected);
        }
        return points;
    }

    public List<Vec3D> getVertices() {
        return Collections.unmodifiableList(points);
    }

    public Color getFillColor() {
        return this.fillColor.get();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor.set(fillColor);
    }
}

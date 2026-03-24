package at.htl.fxglprojection.objects;

import at.htl.fxglprojection.projection.Point3D;
import com.almasb.fxgl.entity.component.Component;
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

    @Override
    public void onAdded() {
        try {
            PolygonRegistry.register(this);
        } catch (DuplicatePolygonException e) {
            throw new RuntimeException(e);
        }
    }

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

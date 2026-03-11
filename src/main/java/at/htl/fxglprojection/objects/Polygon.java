package at.htl.fxglprojection.objects;

import at.htl.fxglprojection.projection.Point3D;
import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.List;

public class Polygon extends Component implements Vertices {
    private List<Point3D> points;

    // https://github.com/AlmasB/FXGLGames/blob/master/Breakout/src/main/java/com/almasb/fxglgames/breakout/components/BallComponent.java
    private ObjectProperty<Color> fillColor = new SimpleObjectProperty<>(Color.GREY);

    public Polygon(List<Point3D> points) {
        this.points = points;

        try {
            PolygonRegistry.registerPolygon(this);
        } catch (DuplicatePolygonException e) {
            throw new RuntimeException(e);
        }
    }

    public Polygon(List<Point3D> points, int expectedVertexCount) {
        if (points.size() != expectedVertexCount)
            throw new InvalidVertexCountException(points.size(), expectedVertexCount);

        this(points);
    }

    public List<Point3D> getVertices() {
        return points;
    }

    public Color getFillColor() {
        return this.fillColor.get();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor.set(fillColor);
    }
}

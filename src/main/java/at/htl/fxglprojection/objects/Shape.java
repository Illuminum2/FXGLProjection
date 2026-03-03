package at.htl.fxglprojection.objects;

import at.htl.fxglprojection.projection.Point3D;
import com.almasb.fxgl.entity.component.Component;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.List;

public abstract class Shape extends Component implements Vertice {
    // https://github.com/AlmasB/FXGLGames/blob/master/Breakout/src/main/java/com/almasb/fxglgames/breakout/components/BallComponent.java
    private ObjectProperty<Color> color = new SimpleObjectProperty<>(Color.GREY);

    public abstract List<Point3D> getVertice();

    public Color getColor() {
        return this.color.get();
    }

    public void setColor(Color color) {
        this.color.set(color);
    }
}

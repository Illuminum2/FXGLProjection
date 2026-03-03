package at.htl.fxglprojection;

import at.htl.fxglprojection.projection.Camera;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class ProjectionApp extends GameApplication {
    private Camera camera;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Projection");
        settings.setVersion("0.1");
    }

    @Override
    protected void initGame() {
        Polygon polygon = new Polygon(
                0,   0,    // vertex 1 (x1, y1)
                100, 0,    // vertex 2
                150, 80,   // vertex 3
                50,  120,  // vertex 4
                -50, 80    // vertex 5
        );
        polygon.setFill(Color.BLUE);
        polygon.setStroke(Color.WHITE);
        polygon.setStrokeWidth(2);

        // Attach as an entity with a view
        entityBuilder()
                .at(300, 200)       // position on screen
                .view(polygon)      // set the Polygon as the view
                .buildAndAttach();
    }

    static void main(String[] args) {
        launch(args);
    }
}
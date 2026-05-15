package at.htl.fxglprojection;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import javafx.scene.input.KeyCode;

import at.htl.fxglprojection.objects.ObjectFactory;
import at.htl.fxglprojection.projection.Vec3D;
import at.htl.fxglprojection.renderer.RenderService;
import at.htl.fxglprojection.projection.Camera3DProjection;

public class ProjectionApp extends GameApplication {
    private final double CAMERA_MOVE_SPEED = 20;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Projection");
        settings.setVersion("0.1");

        settings.addEngineService(RenderService.class);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new ObjectFactory());

        FXGL.spawn("objObject", new SpawnData()
                .put("filepath", "src/main/resources/at/htl/fxglprojection/chicken.obj")
                .put("position", new Vec3D(0, -95, 200))
        );
    }

    @Override
    protected void initInput() {
        Camera3DProjection camera = FXGL.getService(RenderService.class).getCamera();

        FXGL.onKey(KeyCode.W, () -> camera.translatePosition(new Vec3D(0, 0, CAMERA_MOVE_SPEED)));
        FXGL.onKey(KeyCode.S, () -> camera.translatePosition(new Vec3D(0, 0, -CAMERA_MOVE_SPEED)));
        FXGL.onKey(KeyCode.A, () -> camera.translatePosition(new Vec3D(-CAMERA_MOVE_SPEED, 0, 0)));
        FXGL.onKey(KeyCode.D, () -> camera.translatePosition(new Vec3D(CAMERA_MOVE_SPEED, 0, 0)));
        FXGL.onKey(KeyCode.Q, () -> camera.translatePosition(new Vec3D(0, CAMERA_MOVE_SPEED, 0)));
        FXGL.onKey(KeyCode.E, () -> camera.translatePosition(new Vec3D(0, -CAMERA_MOVE_SPEED, 0)));
    }

    static void main(String[] args) {
        launch(args);
    }
}
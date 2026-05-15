package at.htl.fxglprojection;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;

import at.htl.fxglprojection.objects.ObjectFactory;
import at.htl.fxglprojection.projection.Vec3D;
import at.htl.fxglprojection.renderer.RenderService;

public class ProjectionApp extends GameApplication {
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

    static void main(String[] args) {
        launch(args);
    }
}
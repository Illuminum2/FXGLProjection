package at.htl.fxglprojection;

import at.htl.fxglprojection.renderer.TempRenderService;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;

public class ProjectionApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Projection");
        settings.setVersion("0.1");

        settings.addEngineService(TempRenderService.class);
    }

    @Override
    protected void initGame() {
    }

    static void main(String[] args) {
        launch(args);
    }
}
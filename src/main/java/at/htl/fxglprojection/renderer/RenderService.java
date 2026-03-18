package at.htl.fxglprojection.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import com.almasb.fxgl.core.EngineService;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import at.htl.fxglprojection.objects.Polygon3DComponent;
import at.htl.fxglprojection.projection.Camera3DProjection;

// Rendering for planar non-intersecting polygons

public class RenderService extends EngineService {
    private final Pane renderLayer = new Pane();
    private final Map<Polygon3DComponent, Polygon> polygonNodes = new HashMap<>();

    private Camera3DProjection camera;

    @Override
    public void onInit() {
        renderLayer.setMouseTransparent(true);

        getGameScene().getContentRoot().getChildren().add(renderLayer);
    }

    @Override
    public void onUpdate(double tpf) { // onGameUpdate
        // get source data
        // project points
        // depth sort
        // reuse/create Polygon nodes
        // update node coords/fill/stroke
        // remove deleted nodes
    }

    @Override
    public void onExit() {
        renderLayer.getChildren().clear();
        getGameScene().getContentRoot().getChildren().remove(renderLayer);
        polygonNodes.clear();
    }

//    @Nullable
//    private ProjectedPolygon projectPolygon(Polygon3DComponent poly3D) {
//    }
//
//    private void syncNodes(List<Polygon3DComponent> allPolygons) {
//    }
//
//    private void updateFxPolygon(javafx.scene.shape.Polygon fxPoly, ProjectedPolygon pp) {
//    }
}

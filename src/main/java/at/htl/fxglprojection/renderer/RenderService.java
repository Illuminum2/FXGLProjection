package at.htl.fxglprojection.renderer;

import java.util.*;

import javafx.scene.text.Text;
import javafx.scene.layout.Pane;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.core.EngineService;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.Camera3DProjection;

// Rendering for planar non-intersecting polygons

public class RenderService extends EngineService {
    private boolean initialized = false;


    private final Pane renderLayer = new Pane();
    private final PolygonNodeManager polygonNodeManager = new PolygonNodeManager(renderLayer);


    private final Camera3DProjection camera = new Camera3DProjection();

    public Camera3DProjection getCamera() { return camera; }
    private final PolygonProjector polygonProjector = new PolygonProjector(camera);


    private ColorMode colorMode = ColorMode.ORIGINAL;
    private DepthMode depthMode = DepthMode.AVERAGE;

    public ColorMode getColorMode() { return colorMode; }
    public DepthMode getDepthMode() { return depthMode; }
    public void setColorMode(ColorMode colorMode) { this.colorMode = colorMode; }
    public void setDepthMode(DepthMode depthMode) { this.depthMode = depthMode; }


    private boolean showFps = true;
    private final Text fpsText = new Text();
    private boolean showVerticesCount = true;
    private final Text verticesCountText = new Text();

    public boolean getShowFps() { return showFps; }
    public boolean getShowVerticesCount() { return showVerticesCount; }
    public void setShowFps(boolean showFps) { this.showFps = showFps; }
    public void setShowVerticesCount(boolean showVerticesCount) { this.showVerticesCount = showVerticesCount; }


    private boolean enableBackfaceCulling = true;
    private boolean enableFrustumCulling = true;

    public boolean getEnableBackfaceCulling() { return enableBackfaceCulling; }
    public boolean getEnableFrustumCulling() { return enableFrustumCulling; }
    public void setEnableBackfaceCulling(boolean enableBackfaceCulling) { this.enableBackfaceCulling = enableBackfaceCulling; }
    public void setEnableFrustumCulling(boolean enableFrustumCulling) { this.enableFrustumCulling = enableFrustumCulling; }

    @Override
    public void onInit() {
        if (initialized)
            throw new IllegalStateException("RenderService already initialized.");
        initialized = true;

        renderLayer.setMouseTransparent(true);

        FXGL.getGameScene().getContentRoot().getChildren().add(renderLayer);
    }

    @Override
    public void onUpdate(double tpf) { // onGameUpdate
        List<ProjectedPolygon> projectedPolygons = new ArrayList<>();
        polygonProjector.clearVertexCache();

        for (MeshData mesh : GeometryPreprocessor.preprocess()) {
            for (Polygon3D poly : mesh.getRegistered()) {
                ProjectedPolygon projectedPolygon = polygonProjector.project(poly, depthMode, enableBackfaceCulling, enableFrustumCulling);

                if (projectedPolygon != null)
                    projectedPolygons.add(projectedPolygon);
            }
        }

        projectedPolygons.sort(Comparator.comparingDouble(ProjectedPolygon::depth).reversed());

        polygonNodeManager.sync(projectedPolygons, colorMode);

        // FPS counter
        if (showFps) {
            fpsText.setText(Math.round((1.0 / FXGL.tpf())) + " fps");
            fpsText.setY(10);
            renderLayer.getChildren().add(fpsText);
        } else {
            renderLayer.getChildren().remove(fpsText);
        }

        // Vertices counter
        if (showVerticesCount) {
            verticesCountText.setText(projectedPolygons.size() + " vertices");
            verticesCountText.setY(22);
            renderLayer.getChildren().add(verticesCountText);
        } else {
            renderLayer.getChildren().remove(verticesCountText);
        }
    }

    @Override
    public void onExit() {
        renderLayer.getChildren().clear();
        FXGL.getGameScene().getContentRoot().getChildren().remove(renderLayer);
        polygonNodeManager.clearNodes();
        polygonProjector.clearVertexCache();

        initialized = false;
    }
}

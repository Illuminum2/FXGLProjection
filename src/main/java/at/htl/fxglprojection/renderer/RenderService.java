package at.htl.fxglprojection.renderer;

import java.util.*;

import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.core.EngineService;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.Camera3DProjection;

// Rendering for planar non-intersecting polygons

public class RenderService extends EngineService {
    private boolean initialized = false;


    private final Pane renderLayer = new Pane();
    private final Map<Polygon3D, Polygon> polygonNodes = new HashMap<>();


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
    private Text fpsText = new Text();
    private boolean showVerticesCount = true;
    private Text verticesCountText = new Text();

    public boolean getShowFps() { return showFps; }
    public boolean getShowVerticesCount() { return showVerticesCount; }
    public void setShowFps(boolean showFps) { this.showFps = showFps; }
    public void setShowVerticesCount(boolean showVerticesCount) { this.showVerticesCount = showVerticesCount; }


    private boolean enableBackfaceCulling = true;

    public boolean getEnableBackfaceCulling() { return enableBackfaceCulling; }
    public void setEnableBackfaceCulling(boolean enableBackfaceCulling) { this.enableBackfaceCulling = enableBackfaceCulling; }

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

        for (MeshData mesh : GeometryPreprocessor.preprocess()) {
            for (Polygon3D poly : mesh.getRegistered()) {
                ProjectedPolygon projectedPolygon = polygonProjector.project(poly, depthMode, enableBackfaceCulling);

                if (projectedPolygon != null)
                    projectedPolygons.add(projectedPolygon);
            }
        }

        projectedPolygons.sort(Comparator.comparingDouble(ProjectedPolygon::depth).reversed());

        syncNodes(projectedPolygons);

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
        polygonNodes.clear();

        initialized = false;
    }

    private void syncNodes(List<ProjectedPolygon> polygons) {
        Set<Polygon3D> visiblePolygons = new HashSet<>();
        List<Polygon> sortedNodes = new ArrayList<>();

        for (ProjectedPolygon projectedPolygon : polygons) {
            Polygon fxPoly = polygonNodes.get(projectedPolygon.source());

            if (fxPoly == null) {
                fxPoly = new Polygon();
                polygonNodes.put(projectedPolygon.source(), fxPoly);
                renderLayer.getChildren().add(fxPoly);
            }

            updateFxPolygon(fxPoly, projectedPolygon);
            visiblePolygons.add(projectedPolygon.source());
            sortedNodes.add(fxPoly);
        }

        // Remove old/out of view polygons
        polygonNodes.entrySet().removeIf(p -> {
            if (visiblePolygons.contains(p.getKey()))
                return false;

            renderLayer.getChildren().remove(p.getValue());
            return true;
        });

        renderLayer.getChildren().setAll(sortedNodes);
    }

    private void updateFxPolygon(Polygon fxPoly, ProjectedPolygon pp) {
        fxPoly.getPoints().setAll(pp.points());
        fxPoly.setFill(calculateColor(pp));
        fxPoly.setStrokeWidth(2);
    }

    private Color calculateColor(ProjectedPolygon pp) {
        if (colorMode == ColorMode.ORIGINAL)
            return pp.source().getFillColor();
        if (colorMode == ColorMode.NORMALS)
            return Color.rgb(
                    Math.abs((int) (pp.source().getNormal().x * 255)),
                    Math.abs((int) (pp.source().getNormal().y * 255)),
                    Math.abs((int) (pp.source().getNormal().z * 255))
            );
        if (colorMode == ColorMode.QUANTIZED_NORMALS)
            return Color.rgb(
                    Math.abs((int) (Math.round(pp.source().getNormal().x) * 255)),
                    Math.abs((int) (Math.round(pp.source().getNormal().y) * 255)),
                    Math.abs((int) (Math.round(pp.source().getNormal().z) * 255))
            );

        throw new IllegalArgumentException("Illegal color mode " + colorMode.name() + ".");
    }
}

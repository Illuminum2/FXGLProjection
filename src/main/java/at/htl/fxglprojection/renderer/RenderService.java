package at.htl.fxglprojection.renderer;

import org.jetbrains.annotations.Nullable;

import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.core.EngineService;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.Vec3D;
import at.htl.fxglprojection.projection.Camera3DProjection;

// Rendering for planar non-intersecting polygons

public class RenderService extends EngineService {
    private boolean initialized = false;

    private final Pane renderLayer = new Pane();
    private final Map<Polygon3D, Polygon> polygonNodes = new HashMap<>();

    private final Camera3DProjection camera = new Camera3DProjection();

    public Camera3DProjection getCamera() {
        return camera;
    }

    @Override
    public void onInit() {
        if (initialized)
            throw new IllegalStateException("RenderService already initialized.");
        initialized = true;

        renderLayer.setMouseTransparent(true);

        getGameScene().getContentRoot().getChildren().add(renderLayer);
    }

    @Override
    public void onUpdate(double tpf) { // onGameUpdate
        List<ProjectedPolygon> projectedPolygons = new ArrayList<>();

        for (MeshData mesh : GeometryPreprocessor.preprocess()) {
            for (Polygon3D poly : mesh.getRegistered()) {
                ProjectedPolygon projectedPolygon = projectPolygon(poly);

                if (projectedPolygon != null)
                    projectedPolygons.add(projectedPolygon);
            }
        }

        projectedPolygons.sort(Comparator.comparingDouble(ProjectedPolygon::depth).reversed());

        syncNodes(projectedPolygons);
    }

    @Override
    public void onExit() {
        renderLayer.getChildren().clear();
        getGameScene().getContentRoot().getChildren().remove(renderLayer);
        polygonNodes.clear();

        initialized = false;
    }

    @Nullable
    private ProjectedPolygon projectPolygon(Polygon3D poly3D) {
        List<Double> points = new ArrayList<>();
        double depth = 0.0;

        for (Vec3D vertex : poly3D.getVertices()) {
            Vec3D projectedPoint = camera.projectPoint(vertex);

            if (projectedPoint == null)
                return null; // Fixes issue with polygons partially behind camera still getting rendered

            points.add(FXGL.getAppWidth() / 2.0 + projectedPoint.x); // Convert camera-plane x to screen x
            points.add(FXGL.getAppHeight() / 2.0 - projectedPoint.y); // Convert camera-plane y to screen y with y-axis pointing up
            depth += projectedPoint.z;
        }

        return new ProjectedPolygon(poly3D, points, depth / (points.size() /2));
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
        //fxPoly.setFill(pp.getSource().getFillColor());
        fxPoly.setFill(Color.rgb(Math.abs((int) (pp.source().getNormal().x * 255)),Math.abs((int) (pp.source().getNormal().y * 255)), Math.abs((int) (pp.source().getNormal().z * 255))));
        fxPoly.setStrokeWidth(2);
    }
}

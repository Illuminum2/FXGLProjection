package at.htl.fxglprojection.renderer;

import org.jetbrains.annotations.Nullable;

import java.util.*;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.core.EngineService;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.Vec2D;
import at.htl.fxglprojection.projection.Vec3D;
import at.htl.fxglprojection.projection.Camera3DProjection;

// Rendering for planar non-intersecting polygons

public class RenderService extends EngineService {
    private final Pane renderLayer = new Pane();
    private final Map<Polygon3D, Polygon> polygonNodes = new HashMap<>();

    private final Camera3DProjection camera = new Camera3DProjection();

    @Override
    public void onInit() {
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

        syncNodes(projectedPolygons);
    }

    @Override
    public void onExit() {
        renderLayer.getChildren().clear();
        getGameScene().getContentRoot().getChildren().remove(renderLayer);
        polygonNodes.clear();
    }

    @Nullable
    private ProjectedPolygon projectPolygon(Polygon3D poly3D) {
        List<Double> points = new ArrayList<>();

        for (Vec3D vertex : poly3D.getVertices()) {
            Vec2D projectedPoint = camera.projectPoint(vertex);

            if (projectedPoint == null)
                return null; // Fixes issue with polygons partially behind camera still getting rendered

            points.add(FXGL.getAppWidth() / 2.0 + projectedPoint.x); // Convert camera-plane x to screen x
            points.add(FXGL.getAppHeight() / 2.0 - projectedPoint.y); // Convert camera-plane y to screen y with y-axis pointing up
        }

        return new ProjectedPolygon(poly3D, points);
    }

    private void syncNodes(List<ProjectedPolygon> polygons) {
        Set<Polygon3D> visiblePolygons = new HashSet<>();

        for (ProjectedPolygon projectedPolygon : polygons) {
            Polygon fxPoly = polygonNodes.get(projectedPolygon.getSource());

            if (fxPoly == null) {
                fxPoly = new Polygon();
                polygonNodes.put(projectedPolygon.getSource(), fxPoly);
                renderLayer.getChildren().add(fxPoly);
            }

            updateFxPolygon(fxPoly, projectedPolygon);
            visiblePolygons.add(projectedPolygon.getSource());
        }

        // Remove old/out of view polygons
        polygonNodes.entrySet().removeIf(p -> {
            if (visiblePolygons.contains(p.getKey()))
                return false;

            renderLayer.getChildren().remove(p.getValue());
            return true;
        });
    }

    private void updateFxPolygon(javafx.scene.shape.Polygon fxPoly, ProjectedPolygon pp) {
        fxPoly.getPoints().setAll(pp.getPoints());
        fxPoly.setFill(pp.getSource().getFillColor());
        fxPoly.setStrokeWidth(2);
    }
}

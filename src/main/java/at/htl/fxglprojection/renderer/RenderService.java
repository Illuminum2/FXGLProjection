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

    public Camera3DProjection getCamera() { return camera; }

    private ColorMode colorMode = ColorMode.ORIGINAL;
    private DepthMode depthMode = DepthMode.AVERAGE;

    public ColorMode getColorMode() { return colorMode; }
    public DepthMode getDepthMode() { return depthMode; }
    public void setColorMode(ColorMode colorMode) { this.colorMode = colorMode; }
    public void setDepthMode(DepthMode depthMode) { this.depthMode = depthMode; }

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
        List<Double> depthList = new ArrayList<>();

        for (Vec3D vertex : poly3D.getVertices()) {
            Vec3D projectedPoint = camera.projectPoint(vertex);

            if (projectedPoint == null)
                return null; // Fixes issue with polygons partially behind camera still getting rendered

            points.add(FXGL.getAppWidth() / 2.0 + projectedPoint.x); // Convert camera-plane x to screen x
            points.add(FXGL.getAppHeight() / 2.0 - projectedPoint.y); // Convert camera-plane y to screen y with y-axis pointing up

            depthList.add(projectedPoint.z);
        }

        return new ProjectedPolygon(poly3D, points, calculateDepth(depthList));
    }

    private double calculateDepth(List<Double> depth) {
        if (depthMode == DepthMode.MAX)
            return Collections.max(depth);
        if (depthMode == DepthMode.MIN)
            return Collections.min(depth);
        if (depthMode == DepthMode.AVERAGE) {
            Double sum = 0.0;
            for (Double d : depth)
                sum += d;
            return sum / depth.size();
        }
        if (depthMode == DepthMode.MEDIAN) {
            depth.sort(Comparator.naturalOrder());
            int size = depth.size();
            return (size % 2 == 0) ? ((depth.get(size / 2 - 1) + depth.get(size / 2)) / 2.0) : depth.get(size / 2);
        }
        if (depthMode == DepthMode.MID_RANGE) {
            return (Collections.max(depth) + Collections.min(depth)) / 2;
        }
        if (depthMode == DepthMode.WEIGHTED_MID_RANGE) {
            Double sum = 0.0;
            for (Double d : depth)
                sum += d;
            return (Collections.max(depth) + Collections.min(depth) + sum / depth.size()) / 3;
        }
        throw new IllegalArgumentException("Illegal depth mode " + depthMode.name() + ".");
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

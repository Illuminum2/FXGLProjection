package at.htl.fxglprojection.renderer;

import java.util.*;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import at.htl.fxglprojection.objects.Polygon3D;

public class PolygonNodeManager {
    private final Pane renderLayer;
    private final Map<Polygon3D, Polygon> polygonNodes = new HashMap<>();

    PolygonNodeManager(Pane renderLayer) {
        this.renderLayer = renderLayer;
    }

    void clearNodes() {
        polygonNodes.clear();
    }

    public void sync(List<ProjectedPolygon> polygons, ColorMode colorMode) {
        Set<Polygon3D> visiblePolygons = new HashSet<>();
        List<Polygon> sortedNodes = new ArrayList<>();

        for (ProjectedPolygon projectedPolygon : polygons) {
            Polygon fxPoly = polygonNodes.get(projectedPolygon.source());

            if (fxPoly == null) {
                fxPoly = new Polygon();
                polygonNodes.put(projectedPolygon.source(), fxPoly);
                renderLayer.getChildren().add(fxPoly);
            }

            updateFxPolygon(fxPoly, projectedPolygon, colorMode);
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

    private void updateFxPolygon(Polygon fxPoly, ProjectedPolygon pp, ColorMode colorMode) {
        fxPoly.getPoints().setAll(pp.points());
        fxPoly.setFill(calculateColor(pp, colorMode));
        fxPoly.setStrokeWidth(2);
    }

    private Color calculateColor(ProjectedPolygon pp, ColorMode colorMode) {
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

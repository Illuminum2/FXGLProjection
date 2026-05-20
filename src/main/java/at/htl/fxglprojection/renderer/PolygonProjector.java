package at.htl.fxglprojection.renderer;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;

import at.htl.fxglprojection.objects.Polygon3D;
import at.htl.fxglprojection.projection.Camera3DProjection;
import at.htl.fxglprojection.projection.Vec3D;

public class PolygonProjector {
    private final Camera3DProjection camera;

    public PolygonProjector(Camera3DProjection camera) {
        this.camera = camera;
    }

    @Nullable
    public ProjectedPolygon project(Polygon3D poly3D, DepthMode depthMode, boolean enableBackfaceCulling) {
        if (enableBackfaceCulling && isBackFace(poly3D))
            return null;

        List<Double> points = new ArrayList<>();
        List<Double> depthList = new ArrayList<>();

        for (Vec3D vertex : poly3D.getVertices()) {
            Vec3D projectedPoint = camera.projectPoint(vertex);

            if (projectedPoint == null)
                return null; // Fixes issue with polygons partially behind camera still getting rendered

            // .getWidth() and .getHeight() do not work
            points.add(FXGL.getSettings().getActualWidth() / 2.0 + projectedPoint.x); // Convert camera-plane x to screen x
            points.add(FXGL.getSettings().getActualHeight() / 2.0 - projectedPoint.y); // Convert camera-plane y to screen y with y-axis pointing up

            depthList.add(projectedPoint.z);
        }

        return new ProjectedPolygon(poly3D, points, calculateDepth(depthList, depthMode));
    }

    private boolean isBackFace(Polygon3D poly3D) {
        Vec3D center = camera.toCameraSpace(poly3D.getCenter());
        Vec3D normal = camera.toCameraSpaceDirection(poly3D.getNormal());

        return normal.dot(center) >= 0;
    }

    private double calculateDepth(List<Double> depth, DepthMode depthMode) {
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
}

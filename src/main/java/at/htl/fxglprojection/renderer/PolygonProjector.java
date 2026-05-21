package at.htl.fxglprojection.renderer;

import org.jetbrains.annotations.Nullable;

import java.util.*;

import com.almasb.fxgl.dsl.FXGL;

import at.htl.fxglprojection.objects.Polygon3D;
import at.htl.fxglprojection.projection.Camera3DProjection;
import at.htl.fxglprojection.projection.Vec3D;

public class PolygonProjector {
    private final Camera3DProjection camera;
    private final Map<Vec3D, Vec3D> projectedVertexCache = new HashMap<>();

    public PolygonProjector(Camera3DProjection camera) {
        this.camera = camera;
    }

    public void clearVertexCache() {
        projectedVertexCache.clear();
    }

    @Nullable
    public ProjectedPolygon project(Polygon3D poly3D, DepthMode depthMode, boolean enableBackfaceCulling, boolean enableFrustumCulling) {
        if (enableBackfaceCulling && isBackFace(poly3D))
            return null;

        List<Double> points = new ArrayList<>();
        List<Double> depthList = new ArrayList<>();

        // .getWidth() and .getHeight() do not work
        double width = FXGL.getSettings().getActualWidth();
        double height = FXGL.getSettings().getActualHeight();

        boolean allLeft = true, allRight = true, allAbove = true, allBelow = true;

        for (Vec3D vertex : poly3D.getVertices()) {
            Vec3D projectedPoint = projectVertex(vertex, width, height);

            if (projectedPoint == null)
                return null; // Fixes issue with polygons partially behind camera still getting rendered

            double x = projectedPoint.x;
            double y = projectedPoint.y;

            allLeft &= x < 0;
            allRight &= x > width;
            allAbove &= y < 0;
            allBelow &= y > height;

            points.add(x);
            points.add(y);

            depthList.add(projectedPoint.z);
        }

        if (enableFrustumCulling && (allLeft || allRight || allAbove || allBelow))
            return null;

        return new ProjectedPolygon(poly3D, points, calculateDepth(depthList, depthMode));
    }

    @Nullable
    private Vec3D projectVertex(Vec3D vertex, double width, double height) {
        if (projectedVertexCache.containsKey(vertex))
            return projectedVertexCache.get(vertex);

        Vec3D projectedPoint = camera.projectPoint(vertex);

        if (projectedPoint == null)
            return null;

        projectedPoint = new Vec3D(
                width / 2.0 + projectedPoint.x, // Convert camera-plane x to screen x
                height / 2.0 - projectedPoint.y, // Convert camera-plane y to screen y with y-axis pointing up
                projectedPoint.z
        );

        projectedVertexCache.put(vertex, projectedPoint);
        return projectedPoint;
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

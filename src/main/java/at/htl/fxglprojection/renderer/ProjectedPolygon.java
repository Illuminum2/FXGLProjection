package at.htl.fxglprojection.renderer;

import java.util.List;

import at.htl.fxglprojection.objects.Polygon3D;

public class ProjectedPolygon {
    private final Polygon3D source;
    private final List<Double> points;
    private final Double depth;

    public ProjectedPolygon(Polygon3D source, List<Double> points, Double depth) {
        this.source = source;
        this.points = points;
        this.depth = depth;
    }

    public Polygon3D getSource() { return source; }

    public List<Double> getPoints() { return points; }

    public Double getDepth() { return depth; }
}
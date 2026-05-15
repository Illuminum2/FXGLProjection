package at.htl.fxglprojection.renderer;

import java.util.List;

import at.htl.fxglprojection.objects.Polygon3D;

public class ProjectedPolygon {
    private final Polygon3D source;
    private final List<Double> points;

    public ProjectedPolygon(Polygon3D source, List<Double> points) {
        this.source = source;
        this.points = points;
    }

    public Polygon3D getSource() {
        return source;
    }

    public List<Double> getPoints() {
        return points;
    }
}
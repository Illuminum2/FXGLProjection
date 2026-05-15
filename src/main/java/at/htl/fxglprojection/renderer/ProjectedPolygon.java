package at.htl.fxglprojection.renderer;

import at.htl.fxglprojection.objects.Polygon3D;

import java.util.List;

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
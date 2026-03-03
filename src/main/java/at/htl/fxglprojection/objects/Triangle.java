package at.htl.fxglprojection.objects;

import at.htl.fxglprojection.projection.Point3D;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Triangle extends Shape {
    private Point3D p1;
    private Point3D p2;
    private Point3D p3;

    public Triangle(Point3D p1, Point3D p2, Point3D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle(Point3D p1, Point3D p2, Point3D p3, Color color) {
        this(p1, p2, p3);
        setColor(color);
    }

    public List<Point3D> getVertice() {
        List<Point3D> vertice = new ArrayList<>();
        vertice.add(p1);
        vertice.add(p2);
        vertice.add(p3);

        return vertice;
    }
}

package at.htl.fxglprojection.objects;

import java.util.List;

import com.almasb.fxgl.entity.component.Component;

public class Mesh3DComponent extends Component  {
    private final List<Polygon3D> polygons;

    public Mesh3DComponent(List<Polygon3D> polygons) {
        this.polygons = List.copyOf(polygons);
    }

    public List<Polygon3D> getPolygons() {
        return polygons;
    }
}
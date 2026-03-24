package at.htl.fxglprojection.objects;

import java.util.List;

import com.almasb.fxgl.entity.component.Component;

public class Mesh3DComponent extends Component  {
    private final MeshData mesh;

    public Mesh3DComponent(MeshData mesh) {
        this.mesh = mesh;
    }

    public MeshData getMesh() {
        return mesh;
    }
}
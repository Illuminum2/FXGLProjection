package at.htl.fxglprojection.objects;

import java.util.List;

import com.almasb.fxgl.entity.component.Component;

public class Mesh3DComponent extends Component  {
    private final MeshData meshData;

    public Mesh3DComponent(MeshData meshData) {
        this.meshData = meshData;
    }

    public MeshData getMesh() {
        return meshData;
    }
}
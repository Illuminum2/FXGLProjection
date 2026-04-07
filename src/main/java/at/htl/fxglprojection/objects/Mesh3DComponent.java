package at.htl.fxglprojection.objects;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;

@Required(Transform3DComponent.class)
public class Mesh3DComponent extends Component {
    // Injected by FXGL
    private Transform3DComponent transform3DComponent;

    private final MeshData meshData;

    public Mesh3DComponent(MeshData meshData) {
        this.meshData = meshData;
    }

    public MeshData getMesh() {
        return meshData;
    }

    public Transform3DComponent getTransform3DComponent() {
        return transform3DComponent;
    }

    @Override
    public void onAdded() {
        ObjectRegistry.register(this);
    }

    @Override
    public void onRemoved() {
        ObjectRegistry.unregister(this);
    }
}
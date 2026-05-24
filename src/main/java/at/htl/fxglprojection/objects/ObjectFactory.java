package at.htl.fxglprojection.objects;

import java.io.File;
import java.io.IOException;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.SpawnData;

import at.htl.fxglprojection.projection.Vec3D;
import at.htl.fxglprojection.projection.Quaternion;

public class ObjectFactory implements EntityFactory {
    @Spawns("objObject")
    public Entity spawnObj(SpawnData spawnData) throws ObjFormatException {
        if (!spawnData.hasKey("filepath") || !(spawnData.get("filepath") instanceof String))
            throw new ObjFormatException("Missing obj filepath.");

        File objFile = new File((String) spawnData.get("filepath"));
        MeshData meshData;

        boolean skipUnsupportedFeatures = (boolean) spawnData.getData().getOrDefault("skipUnsupportedFeatures", false);

        try {
            meshData = MeshAssetCache.get(objFile, skipUnsupportedFeatures);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Transform3DComponent transform = new Transform3DComponent();

        if (spawnData.hasKey("position") && spawnData.get("position") instanceof Vec3D)
            transform.setPosition(spawnData.get("position"));
        if (spawnData.hasKey("scale") && spawnData.get("scale") instanceof Vec3D)
            transform.setScale(spawnData.get("scale"));
        if (spawnData.hasKey("rotationQuat") && spawnData.get("rotationQuat") instanceof Quaternion)
            transform.setRotationQuat(spawnData.get("rotationQuat"));

        return FXGL.entityBuilder()
                .type(ObjectType.OBJECT)
                .with(transform)
                .with(new Mesh3DComponent(meshData))
                .buildAndAttach();
    }
}

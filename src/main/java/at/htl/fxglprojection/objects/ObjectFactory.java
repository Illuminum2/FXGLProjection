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

        try {
            meshData = ObjParser.parseFile(objFile);
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

//    @Spawns("polygon")
//    public Entity newPolygon(SpawnData data) {
//        Polygon3D polygon = new Polygon3D(parsePolygonData(data));
//
//        return entityBuilder(data)
//                .type(SHAPE)
//                //.with(polygon)
//                .build();
//    }

//    private List<Vec3D> parsePolygonData(SpawnData data) {
//        // Tree map sorts by key automatically
//        Map<Integer, Vec3D> points = new TreeMap<>();
//
//        Pattern r = Pattern.compile("^p(\\d+$)");
//
//        data.getData().forEach((String key, Object value) -> {
//            Matcher m = r.matcher(key);
//
//            if (value instanceof Vec3D && m.matches()) {
//                points.put(Integer.parseInt(m.group(1)), (Vec3D) value);
//            }
//        });
//
//        List<Vec3D> pointList = points.values().stream().toList();
//
//        if (data.hasKey("origin") && data.get("origin") instanceof Vec3D) {
//            Vec3D origin = data.get("origin");
//
//            // Normalize to origin
//            for (Vec3D point : pointList) {
//                point.x += origin.x;
//                point.y += origin.y;
//                point.z += origin.z;
//            }
//        }
//
//        return pointList;
//    }
}

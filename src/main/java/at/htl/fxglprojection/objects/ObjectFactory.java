package at.htl.fxglprojection.objects;

import java.io.File;
import java.io.IOException;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.SpawnData;

import at.htl.fxglprojection.projection.Vec3D;

public class ObjectFactory implements EntityFactory {
    @Spawns("objObject")
    public Entity spawnObj(SpawnData spawnData) {
        File objFile = new File((String) spawnData.get("filepath"));
        MeshData meshData;

        try {
            meshData = ObjParser.parseFile(objFile);
        } catch (ObjFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Entity e = FXGL.entityBuilder()
                .type(ObjectType.OBJECT)
                .with(new Transform3DComponent())
                .with(new Mesh3DComponent(meshData))
                .buildAndAttach();

        if (spawnData.hasKey("position") && spawnData.get("position") instanceof Vec3D)
            e.getComponent(Transform3DComponent.class).setPosition(spawnData.get("position"));

        return e;
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

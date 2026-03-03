package at.htl.fxglprojection;

import static com.almasb.fxgl.dsl.FXGL.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import at.htl.fxglprojection.projection.Point3D;
import at.htl.fxglprojection.objects.Triangle;
import at.htl.fxglprojection.objects.Quad;

import static at.htl.fxglprojection.ObjectType.*;

public class ObjectFactory implements EntityFactory {
    @Spawns("triangle")
    public Entity newTriangle(SpawnData data) {
        Point3D p1 = data.get("p1");
        Point3D p2 = data.get("p2");
        Point3D p3 = data.get("p3");

        return entityBuilder(data)
                .type(TRIANGLE)
                .with(new Triangle(p1, p2, p3))
                .build();
    }

    @Spawns("quad")
    public Entity newQuad(SpawnData data) {
        Point3D p1 = data.get("p1");
        Point3D p2 = data.get("p2");
        Point3D p3 = data.get("p3");
        Point3D p4 = data.get("p4");

        return entityBuilder(data)
                .type(QUAD)
                .with(new Quad(p1, p2, p3, p4))
                .build();
    }
}

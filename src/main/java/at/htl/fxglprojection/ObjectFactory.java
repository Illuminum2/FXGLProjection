package at.htl.fxglprojection;

import static com.almasb.fxgl.dsl.FXGL.*;

import at.htl.fxglprojection.objects.Polygon;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;

import at.htl.fxglprojection.projection.Point3D;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static at.htl.fxglprojection.ObjectType.*;

public class ObjectFactory implements EntityFactory {
    @Spawns("shape")
    public Entity newPolygon(SpawnData data) {
        // Tree map sorts by key automatically
        Map<Integer, Point3D> points = new TreeMap<>();

        Pattern r = Pattern.compile("^p(\\d+$)");

        data.getData().forEach((String key, Object value) -> {
            Matcher m = r.matcher(key);

            if (value instanceof Point3D && m.matches()) {
                points.put(Integer.getInteger(m.group(0)), (Point3D) value);
            }
        });

        Polygon polygon;

        if (data.hasKey("origin") && data.get("origin") instanceof Point3D) {
            polygon = new Polygon(data.get("origin"), points.values().stream().toList());
        } else {
            polygon = new Polygon(points.values().stream().toList());
        }

        return entityBuilder(data)
                .type(SHAPE)
                .with(polygon)
                .build();
    }
}

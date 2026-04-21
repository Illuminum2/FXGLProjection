package at.htl.fxglprojection.renderer;

import java.util.ArrayList;
import java.util.List;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.MathHelper;
import at.htl.fxglprojection.projection.Quaternion;
import at.htl.fxglprojection.projection.Vec3D;

public class GeometryPreprocessor {
    public static List<MeshData> preprocess() throws DuplicatePolygonException {
        List<MeshData> mesh_processed = new ArrayList<>();

        for (Mesh3DComponent meshComp : ObjectRegistry.getRegistered()) {
            Transform3DComponent transform = meshComp.getTransform3DComponent();

            MeshData meshData_processed = new MeshData();

            for (Polygon3D p : meshComp.getMesh().getRegistered()) {
                List<Vec3D> v_processed = new ArrayList<>();

                for (Vec3D v : p.getVertices()) {
                    v_processed.add(applyTransform(v, transform));
                }

                Vec3D n_processed = MathHelper.vectorQuaternionRotation(p.getNormal(), transform.getRotationQuat());

                Polygon3D processedPolygon = new Polygon3D(v_processed, n_processed, p.getFillColor());
                meshData_processed.register(processedPolygon);
            }

            mesh_processed.add(meshData_processed);
        }

        return mesh_processed;
    }

    private static Vec3D applyTransform(Vec3D v, Transform3DComponent transform) {
        Vec3D scale = transform.getScale();
        Quaternion rotation = transform.getRotationQuat();
        Vec3D position = transform.getPosition();

        Vec3D v_processed = new Vec3D(v.x, v.y, v.z);

        v_processed.x *= scale.x;
        v_processed.y *= scale.y;
        v_processed.z *= scale.z;

        v_processed = MathHelper.vectorQuaternionRotation(v_processed, rotation);

        v_processed.x += position.x;
        v_processed.y += position.y;
        v_processed.z += position.z;

        return v_processed;
    }
}

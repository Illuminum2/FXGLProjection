package at.htl.fxglprojection.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.MathHelper;
import at.htl.fxglprojection.projection.Vec3D;

public class GeometryPreprocessor {
    private static final Map<Mesh3DComponent, MeshData> processedCache = new HashMap<>();

    public static List<MeshData> preprocess() {
        List<MeshData> mesh_processed = new ArrayList<>();

        boolean registryChanged = ObjectRegistry.changedSinceRead();
        List<Mesh3DComponent> meshComponents = ObjectRegistry.getRegistered();

        if (registryChanged)
            processedCache.keySet().removeIf(m -> !meshComponents.contains(m));

        for (Mesh3DComponent meshComp : meshComponents) {
            Transform3DComponent transform = meshComp.getTransform3DComponent();

            MeshData meshData = meshComp.getMesh();
            MeshData processedMesh = processedCache.get(meshComp);

            if (processedMesh == null || meshData.changedSinceRead() || transform.changedSinceRead()) {
                processedMesh = preprocessMesh(meshComp, transform);
                processedCache.put(meshComp, processedMesh);
            }

            mesh_processed.add(processedMesh);
        }

        return mesh_processed;
    }

    private static MeshData preprocessMesh(Mesh3DComponent meshComp, Transform3DComponent transform) {
        MeshData meshData_processed = new MeshData();

        Vec3D position = transform.getPosition();
        Vec3D scale = transform.getScale();
        double[][] rotation = MathHelper.quaternionToMatrix(transform.getRotationQuat());

        for (Polygon3D p : meshComp.getMesh().getRegistered()) {
            List<Vec3D> v_processed = new ArrayList<>();

            for (Vec3D v : p.getVertices()) {
                v_processed.add(applyTransform(v, position, scale, rotation));
            }

            Vec3D n_processed = MathHelper.matrixVectorMultiply(rotation, p.getNormal());

            Polygon3D processedPolygon = new Polygon3D(v_processed, n_processed, p.getFillColor());
            try {
                meshData_processed.register(processedPolygon);
            } catch (DuplicatePolygonException _) {} // Exception is impossible
        }

        return meshData_processed;
    }

    private static Vec3D applyTransform(Vec3D v, Vec3D position, Vec3D scale, double[][] rotation) {
        Vec3D v_processed = new Vec3D(
                rotation[0][0] * v.x + rotation[0][1] * v.y + rotation[0][2] * v.z,
                rotation[1][0] * v.x + rotation[1][1] * v.y + rotation[1][2] * v.z,
                rotation[2][0] * v.x + rotation[2][1] * v.y + rotation[2][2] * v.z
        );

        v_processed.x *= scale.x;
        v_processed.y *= scale.y;
        v_processed.z *= scale.z;

        v_processed.x += position.x;
        v_processed.y += position.y;
        v_processed.z += position.z;

        return v_processed;
    }
}

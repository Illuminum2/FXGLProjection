package at.htl.fxglprojection.renderer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.Vec2D;
import at.htl.fxglprojection.projection.Vec3D;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import com.almasb.fxgl.core.EngineService;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import at.htl.fxglprojection.projection.Camera3DProjection;

// Rendering for planar non-intersecting polygons

public class TempRenderService extends EngineService {
    private final Pane renderLayer = new Pane();
    private final Map<Polygon3D, Polygon> polygonNodes = new HashMap<>();

    private Camera3DProjection camera;

    // Temporary implementation
    private Transform3DComponent transform;
    private Mesh3DComponent mesh;
    private Entity entity;

    @Override
    public void onInit() {
        renderLayer.setMouseTransparent(true);

        getGameScene().getContentRoot().getChildren().add(renderLayer);

        try {
            mesh = new Mesh3DComponent(
                    ObjParser.parseFile(new File("src/main/resources/at/htl/fxglprojection/chicken.obj"))
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        tempFrame();
    }

    // Temporary implementation
    public void tempFrame() {
        long start = System.currentTimeMillis();

        if (camera == null)
            camera = new Camera3DProjection();

        if  (transform == null) {
            transform = new Transform3DComponent();
            transform.setPosition(new Vec3D(0, -95, 200));
        }

        if (entity == null) {
            try {
                entity = FXGL.entityBuilder()
                        .type(ObjectType.OBJECT)
                        .with(transform)
                        .with(mesh)
                        .buildAndAttach();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        renderLayer.getChildren().clear();

        List<MeshData> sceneMesh = GeometryPreprocessor.preprocess();

        for (MeshData mesh : sceneMesh) {
            for (Polygon3D poly : mesh.getRegistered()) {
                List<Vec3D> vertices = poly.getVertices();
                List<Double> projectedPoints = new ArrayList<>();
                boolean behindCamera = false;

                for (Vec3D vertex : vertices) {
                    Vec3D projectedPoint = camera.projectPoint(vertex);

                    if (projectedPoint == null) {
                        behindCamera = true;
                        break;
                    }

                    projectedPoints.add(FXGL.getAppWidth() / 2.0 + projectedPoint.x); // Convert camera-plane x to screen x.
                    projectedPoints.add(FXGL.getAppHeight() / 2.0 - projectedPoint.y); // Convert camera-plane y to screen y with y-axis pointing up
                }

                if (behindCamera)
                    continue;

                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(projectedPoints);
                polygon.setFill(poly.getFillColor());
                polygon.setStrokeWidth(2);

                renderLayer.getChildren().add(polygon);
            }
        }

        System.out.println("Finished in: " + (System.currentTimeMillis() - start));
    }

    @Override
    public void onUpdate(double tpf) { // onGameUpdate
        // get source data
        // project points
        // depth sort
        // reuse/create Polygon nodes
        // update node coords/fill/stroke
        // remove deleted nodes
    }

    @Override
    public void onExit() {
        renderLayer.getChildren().clear();
        getGameScene().getContentRoot().getChildren().remove(renderLayer);
        polygonNodes.clear();
    }

//    @Nullable
//    private ProjectedPolygon projectPolygon(Polygon3DComponent poly3D) {
//    }
//
//    private void syncNodes(List<Polygon3DComponent> allPolygons) {
//    }
//
//    private void updateFxPolygon(javafx.scene.shape.Polygon fxPoly, ProjectedPolygon pp) {
//    }
}

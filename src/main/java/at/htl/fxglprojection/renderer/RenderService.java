package at.htl.fxglprojection.renderer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.htl.fxglprojection.objects.*;
import at.htl.fxglprojection.projection.Vec2D;
import at.htl.fxglprojection.projection.Vec3D;
import com.almasb.fxgl.app.scene.Camera3D;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import com.almasb.fxgl.core.EngineService;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

import at.htl.fxglprojection.projection.Camera3DProjection;

// Rendering for planar non-intersecting polygons

public class RenderService extends EngineService {
    private final Pane renderLayer = new Pane();
    private final Map<Polygon3D, Polygon> polygonNodes = new HashMap<>();

    private Camera3DProjection camera;

    @Override
    public void onInit() {
        renderLayer.setMouseTransparent(true);

        getGameScene().getContentRoot().getChildren().add(renderLayer);

        // Temporary implementation
        Camera3DProjection camera = new Camera3DProjection();

        try {
            Transform3DComponent transform = new Transform3DComponent();
            Mesh3DComponent mesh = new Mesh3DComponent(
                    ObjParser.parseFile(new File("src/main/resources/at/htl/fxglprojection/chicken.obj"))
            );

            transform.setPosition(new Vec3D(0,0,0));

            Entity e = FXGL.entityBuilder()
                    .type(ObjectType.OBJECT)
                    .with(transform)
                    .with(mesh)
                    .buildAndAttach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long start = System.currentTimeMillis();
        List<MeshData> sceneMesh = GeometryPreprocessor.preprocess();
        for (MeshData mesh : sceneMesh) {
            for (Polygon3D poly : mesh.getRegistered()) {
                List<Vec3D> vertices = poly.getVertices();
                List<Double> projectedPoints = new ArrayList<>();

                for (Vec3D vertex : vertices) {
                    System.out.println("X: " + vertex.x);
                    System.out.println("Y: " + vertex.y);
                    System.out.println("Z: " + vertex.z);

                    Vec2D projectedPoint = camera.projectPoint(vertex);

                    System.out.println(projectedPoint);
                    if (projectedPoint != null) {
                        projectedPoints.add(projectedPoint.x);
                        projectedPoints.add(projectedPoint.y);
                    }
                }

                Polygon polygon = new Polygon();
                polygon.getPoints().addAll(projectedPoints);
                polygon.setFill(poly.getFillColor());
                polygon.setStroke(poly.getFillColor());
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

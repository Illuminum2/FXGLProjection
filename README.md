# FXGLProjection

This is a 3D projection layer for FXGL and JavaFX. Instead of using a full 3D renderer, it keeps mesh data inside normal FXGL components, projects it through a custom camera class, and draws the output with JavaFX Polygon nodes while remaining compatible with the FXGL entity structure.

**Limitations:** The renderer is built for planar, non-intersecting polygons. OBJ material files and UV rendering are outside of the current render scope.

## Run

```bash
./mvnw clean javafx:run
```

The project uses Java 25, FXGL 25, and JavaFX 21.0.6.

## Controls

Use `WASD` for camera-relative horizontal movement, `Q/E` for vertical movement, and the arrow keys to rotate the view.

## Usage

Register the renderer as an FXGL engine service:

```java
settings.addEngineService(RenderService.class);
```

Load the included chicken OBJ:

```java
FXGL.spawn("objObject", new SpawnData()
        .put("filepath", "src/main/resources/at/htl/fxglprojection/chicken.obj")
        .put("position", new Vec3D(0, -95, 200))
);
```

Switch renderer modes:

```java
RenderService renderer = FXGL.getService(RenderService.class);

renderer.setColorMode(ColorMode.NORMALS);
renderer.setDepthMode(DepthMode.AVERAGE);
```

Move or tune the camera:

```java
Camera3DProjection camera = renderer.getCamera();

camera.translatePosition(new Vec3D(0, 0, 1));
camera.rotateView(new Vec3D(0, 1, 0));
camera.setFocalLength(320);
```

## Features

- **OBJ loading:** Loads OBJ vertices, normals, faces.

- **Backface culling:** Removes polygons facing away from the camera.


- **Quaternion rotation:** Quaternions for camera and mesh rotation to avoid gimbal lock.

- **Camera-space movement:** Movement follows the current camera view direction.

- **Custom projection:** Projects 3D mesh data into 2D JavaFX `Polygon` nodes.

- **Mesh transforms:** Allows applying scale, rotation, and translation.

- **Depth sorting:** Selectable depth sorting approaches for ordering projected polygons.

- **Color modes:** Selectable coloring approaches for projected polygons:
    - `ORIGINAL`: Polygon fill color.
    - `NORMALS`: Normals to RGB.
    - `QUANTIZED_NORMALS`: Rounded normals to RGB.

- **Node reuse:** Reuses JavaFX `Polygon` nodes between frames.

## Architecture

- `ObjectFactory`
  - Defines the `objObject` spawn type. It reads the OBJ path from `SpawnData`, parses it into `MeshData`, creates a transform, and builds an entity with the resulting `Transform3DComponent` and `Mesh3DComponent`.

- `ObjParser`
  - Loads OBJ files into the internal mesh format. It supports `v`, `vn`, and `f v/vt/vn` definitions, including negative indices.

- `Transform3DComponent`
  - Stores position, scale, and quaternion rotation. The quaternion is converted into a rotation matrix during preprocessing.

- `Mesh3DComponent`
  - Holds the mesh for an entity. When FXGL adds or removes it, the component registers or unregisters itself with `ObjectRegistry`.

- `ObjectRegistry`
  - Keeps the current list of renderable mesh components to avoid searching the FXGL world every frame.

- `MeshData` and `Polygon3D`
  - `MeshData` is a polygon container, `Polygon3D` stores vertices, one flat normal, and a fill color.

- `GeometryPreprocessor`
  - Converts mesh vertices into world-space vertices by applying scale, rotation, and translation from the mesh `Transform3DComponent`. Also rotates normals.

- `Camera3DProjection`
  - Stores camera position, focal length, movement/rotation speed, and quaternion view rotation. It projects world-space points into camera space, then into 2D + depth.

- `RenderService`
  - Runs every frame; takes preprocessed meshes from the `GeometryPreprocessor`, initiates projection, orders visible polygons, and manages the render layer.

- `PolygonProjector`
  - Projects world-space polygons with `Camera3DProjection`, applies backface culling, and calculates depth values for sorting.

- `PolygonNodeManager`
  - Keeps JavaFX `Polygon` nodes in sync with currently visible polygons, including color modes and node reuse.

## Projection

`Camera3DProjection.projectPoint()` subtracts the camera position, transforms the point into camera space using the inverse camera rotation matrix and camera position, and applies perspective projection:

```text
x' = focalLength * x / z
y' = focalLength * y / z
```

Camera rotation is stores as a quaternion.

## Optimizations

- Backface culling
  - `PolygonProjector` detects faces pointing away from the camera view direction and stops them from being drawn.

- Processed mesh cache
  - `GeometryPreprocessor` stores processed meshes by `Mesh3DComponent` and recomputes them only when the mesh data or transform changes.

- Shared transformed vertices
  - During preprocessing, repeated source vertices are transformed once and reused across polygons. This matters for OBJ meshes, where many faces share vertices.

- JavaFX node reuse
  - `RenderService` keeps track of existing JavaFX `Polygon` nodes, updating them each frame, and removing them only once they are no longer visible/culled.
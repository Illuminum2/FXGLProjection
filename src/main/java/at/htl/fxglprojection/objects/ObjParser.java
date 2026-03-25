package at.htl.fxglprojection.objects;

import at.htl.fxglprojection.projection.Vec3D;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class ObjParser {
    // https://nullprogram.com/blog/2025/03/02/
    // https://www.scratchapixel.com/lessons/3d-basic-rendering/obj-file-format/obj-file-format.html
    public static MeshData parseFile(File file) throws IOException, DataFormatException {
        FileReader fr = new FileReader(file);

        List<Vec3D> vertices = new ArrayList<>();
        List<Vec3D> normals = new ArrayList<>();
        List<Polygon3D> faces = new ArrayList<>();

        for (String l : fr.readAllLines()) {
            if (l.isBlank())
                continue;

            String[] tokens = l.split("\\s+");

            try {
                if (tokens[0].equals("v") && tokens.length == 4) {
                    Vec3D p = new Vec3D(
                            Double.parseDouble(tokens[1]),
                            Double.parseDouble(tokens[2]),
                            Double.parseDouble(tokens[3])
                    );

                    vertices.add(p);
                } else if (tokens[0].equals("vn") && tokens.length == 4) {
                    Vec3D n = new Vec3D(
                            Double.parseDouble(tokens[1]),
                            Double.parseDouble(tokens[2]),
                            Double.parseDouble(tokens[3])
                    );

                    normals.add(n);
                } else if (tokens[0].equals("f") && tokens.length >= 4) {
                    List<Vec3D> faceVertices = new ArrayList<>();
                    Vec3D faceNormal = new Vec3D(); // Only one normal per face because rendering only works with flat polygons; initialized to make compiler shut up

                    for (int i = tokens.length - 1; i > 0; i--) {
                        String[] indices = tokens[i].split("/"); // Escaping is redundant

                        if (indices.length != 3)
                            throw new DataFormatException("Obj face is not correctly formatted.");

                        int normalIndex = Integer.parseInt(indices[2]) - 1; // Normals use 1-indexing
                        if (normalIndex < 0)
                            normalIndex += normals.size() + 1;

                        faceNormal = normals.get(normalIndex);

                        int verticesIndex = Integer.parseInt(indices[0]) - 1; // Vertices use 1-indexing
                        if (verticesIndex < 0)
                            verticesIndex += normals.size() + 1;

                        faceVertices.add(vertices.get(verticesIndex));
                    }

                    Polygon3D face = new Polygon3D(faceVertices, faceNormal);
                    faces.add(face);
                } else if (tokens[0].equals("o")) {
                    throw new DataFormatException("Obj file contains object definitions, this is not supported.");
                } else if (!tokens[0].equals("vt") && !tokens[0].equals("g")){
                    throw new DataFormatException("Obj file is not correctly structured.");
                }
            } catch (NumberFormatException e) {
                throw new DataFormatException(e.getMessage());
            }
        }

        MeshData meshData = new MeshData();

        try {
            meshData.register(faces);
        } catch (DuplicatePolygonException e) {
            throw new DataFormatException("Obj file contains duplicate vertices.");
        }

        return meshData;
    }
}

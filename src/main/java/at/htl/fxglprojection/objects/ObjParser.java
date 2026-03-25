package at.htl.fxglprojection.objects;

import at.htl.fxglprojection.projection.Vec3D;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjParser {
    // https://nullprogram.com/blog/2025/03/02/
    // https://www.scratchapixel.com/lessons/3d-basic-rendering/obj-file-format/obj-file-format.html
    public static MeshData parseFile(File file) throws IOException, ObjFormatException {
        FileReader fr = new FileReader(file);

        List<Vec3D> vertices = new ArrayList<>();
        List<Vec3D> normals = new ArrayList<>();
        List<Polygon3D> faces = new ArrayList<>();

        for (String l : fr.readAllLines()) {
            if (l.isBlank() || l.charAt(0) == '#')
                continue;

            String[] tokens = l.split("\\s+");

            if (tokens[0].equals("v")) {
                vertices.add(parseVertex(tokens));
            } else if (tokens[0].equals("vn")) {
                normals.add(parseNormal(tokens));
            } else if (tokens[0].equals("f")) {
                faces.add(parseFace(tokens, vertices, normals));
            } else if (tokens[0].equals("o")) {
                throw new ObjFormatException("File contains unsupported object definitions.");
            } else if (!tokens[0].equals("vt") && !tokens[0].equals("g")){
                throw new ObjFormatException("Malformed line.");
            }
        }

        MeshData meshData = new MeshData();

        try {
            meshData.register(faces);
        } catch (DuplicatePolygonException e) {
            throw new ObjFormatException("File contains duplicate vertices.");
        }

        return meshData;
    }

    private static Vec3D parseVertex(String[] tokens) throws ObjFormatException {
        if (tokens.length != 4)
            throw new ObjFormatException("Malformed vertex definition.");

        try {
            return new Vec3D(
                    Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2]),
                    Double.parseDouble(tokens[3])
            );
        } catch (NumberFormatException e) {
            throw new ObjFormatException("Invalid numeric format in vertex definition.");
        }
    }

    private static Vec3D parseNormal(String[] tokens) throws ObjFormatException {
        if (tokens.length != 4)
            throw new ObjFormatException("Malformed normal definition.");

        try {
            return new Vec3D(
                    Double.parseDouble(tokens[1]),
                    Double.parseDouble(tokens[2]),
                    Double.parseDouble(tokens[3])
            );
        } catch (NumberFormatException e) {
            throw new ObjFormatException("Invalid numeric format in normal definition.");
        }
    }

    private static Polygon3D parseFace(String[] tokens, List<Vec3D> vertices, List<Vec3D> normals) throws ObjFormatException {
        if (tokens.length >= 4)
            throw new ObjFormatException("Malformed face definition.");

        List<Vec3D> faceVertices = new ArrayList<>();
        Vec3D faceNormal = new Vec3D(); // Only one normal per face because rendering only works with flat polygons; initialized to make compiler shut up

        try {
            for (int i = tokens.length - 1; i > 0; i--) {
                String[] indices = tokens[i].split("/"); // Escaping is redundant

                if (indices.length != 3)
                    throw new ObjFormatException("Malformed face definition.");

                int normalIndex = Integer.parseInt(indices[2]) - 1; // Normals use 1-indexing
                if (normalIndex < 0)
                    normalIndex += normals.size() + 1;

                faceNormal = normals.get(normalIndex);

                int verticesIndex = Integer.parseInt(indices[0]) - 1; // Vertices use 1-indexing
                if (verticesIndex < 0)
                    verticesIndex += normals.size() + 1;

                faceVertices.add(vertices.get(verticesIndex));
            }
        } catch (NumberFormatException e) {
            throw new ObjFormatException("Invalid numeric format in face definition.");
        }

        return new Polygon3D(faceVertices, faceNormal);
    }
}

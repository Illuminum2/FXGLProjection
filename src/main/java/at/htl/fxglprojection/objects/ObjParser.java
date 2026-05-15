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

        int line = 1;

        for (String l : fr.readAllLines()) {
            line++;

            l = l.trim();
            if (l.isBlank() || l.charAt(0) == '#')
                continue;

            String[] tokens = l.split("\\s+");

            try {
                if (tokens[0].equals("v")) {
                    vertices.add(parseVertex(tokens));
                } else if (tokens[0].equals("vn")) {
                    normals.add(parseNormal(tokens));
                } else if (tokens[0].equals("f")) {
                    faces.add(parseFace(tokens, vertices, normals));
                } else if (tokens[0].equals("o")) {
                    throw new ObjFormatException("File contains unsupported object definitions.");
                } else if (!tokens[0].equals("vt") && !tokens[0].equals("g")) {
                    throw new ObjFormatException("Malformed line.");
                }
            } catch (ObjFormatException e) {
                e.setLine(line);
                throw e;
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
        if (tokens.length < 4)
            throw new ObjFormatException("Malformed face definition.");

        List<Vec3D> faceVertices = new ArrayList<>();
        Vec3D faceNormal = new Vec3D(); // Only one normal per face because rendering only works with flat polygons; initialized to make compiler shut up

        try {
            for (int i = 1; i < tokens.length; i++) {
                String[] indices = tokens[i].split("/"); // Escaping is redundant

                if (indices.length != 3)
                    throw new ObjFormatException("Malformed face definition.");

                int normalIndex = parseObjIndex(indices[2], normals.size());
                faceNormal = normals.get(normalIndex);

                int verticesIndex = parseObjIndex(indices[0], vertices.size());
                faceVertices.add(vertices.get(verticesIndex));
            }
        } catch (NumberFormatException e) {
            throw new ObjFormatException("Invalid numeric format in face definition.");
        }

        return new Polygon3D(faceVertices, faceNormal);
    }

    private static int parseObjIndex(String token, int size) throws ObjFormatException {
        int index = Integer.parseInt(token) - 1; // Vertices and faces use 1-indexing

        if (index == -1)
            throw new ObjFormatException("Zero index in obj definition.");

        if (index < 0)
            index += size + 1;

        if (index < 0 || index >= size)
            throw new ObjFormatException("Out-of-bounds index in obj definition.");

        return index;
    }
}

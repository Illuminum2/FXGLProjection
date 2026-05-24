package at.htl.fxglprojection.objects;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MeshAssetCache {
    private static final Map<String, MeshData> meshDataCache = new HashMap<>();

    public static MeshData get(File file, boolean skipUnsupportedFeatures) throws IOException, ObjFormatException {
        MeshData meshData = meshDataCache.get(file.getCanonicalPath()); // Canonical path returns full actual path

        if (meshData == null) {
            meshData = ObjParser.parseFile(file, skipUnsupportedFeatures);
            meshData.makeImmutable();
            meshDataCache.put(file.getCanonicalPath(), meshData);
        }

        return meshData.mutableCopy();
    }
}

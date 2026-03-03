package at.htl.fxglprojection.projection;

public class MathHelper {
    // Quaternion

    public static double[][] quaternionToMatrix(Quaternion q) {
        return new double[][] {
                { 1 - 2 * q.y * q.y - 2 * q.z * q.z, 2 * q.x * q.y - 2 * q.z * q.w, 2 * q.x * q.z + 2 * q.y * q.w },
                { 2 * q.x * q.y + 2 * q.z * q.w, 1 - 2 * q.x * q.x - 2 * q.z * q.z, 2 * q.y * q.z - 2 * q.x * q.w },
                { 2 * q.x * q.z - 2 * q.y * q.w, 2 * q.y * q.z + 2 * q.x * q.w, 1 - 2 * q.x * q.x - 2 * q.y * q.y }
        };
    }

    public static Quaternion quaternionMultiply(Quaternion q1, Quaternion q2) {
        return new Quaternion (
                q1.w * q2.w - q1.x * q2.x - q1.y * q2.y - q1.z * q2.z,
                q1.w * q2.x + q1.x * q2.w + q1.y * q2.z - q1.z * q2.y,
                q1.w * q2.y - q1.x * q2.z + q1.y * q2.w + q1.z * q2.x,
                q1.w * q2.z + q1.x * q2.y - q1.y * q2.x + q1.z * q2.w
        );
    }

    public static Quaternion quaternionNormalize(Quaternion q) {
        double length = Math.sqrt(q.w * q.w + q.x * q.x + q.y * q.y + q.z * q.z);
        return new Quaternion(q.w / length, q.x / length, q.y / length, q.z / length);
    }

    // Matrix

    public static double[][] matrixTranspose(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        double[][] transposed = new double[n][m];

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                transposed[x][y] = matrix[y][x];
            }
        }

        return transposed;
    }
}

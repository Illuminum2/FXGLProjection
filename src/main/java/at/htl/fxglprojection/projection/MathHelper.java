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

    // https://math.stackexchange.com/questions/40164/how-do-you-rotate-a-vector-by-a-unit-quaternion
    public static Vec3D vectorQuaternionRotation(Vec3D v, Quaternion q) {
        Quaternion v_q = new Quaternion(0, v.x, v.y, v.z);
        Quaternion q_conj = new Quaternion(q.w,-1 * q.x,-1 * q.y,-1 * q.z);
        Quaternion q_temp = quaternionMultiply(quaternionMultiply(q, v_q),q_conj);

        return new Vec3D(q_temp.x, q_temp.y, q_temp.z);
    }

    // Matrix

    public static Vec3D matrixVectorMultiply(double[][] matrix, Vec3D v) {
        return new Vec3D(
                matrix[0][0] * v.x + matrix[0][1] * v.y + matrix[0][2] * v.z,
                matrix[1][0] * v.x + matrix[1][1] * v.y + matrix[1][2] * v.z,
                matrix[2][0] * v.x + matrix[2][1] * v.y + matrix[2][2] * v.z
        );
    }

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

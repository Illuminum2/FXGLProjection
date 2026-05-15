package at.htl.fxglprojection.projection;

import org.jetbrains.annotations.Nullable;

public class Camera3DProjection {
    private Vec3D position;
    private Quaternion quaternion;

    private double[][] R;

    private double focalLength;
    private double speed;

    public Camera3DProjection(Vec3D p, double focalLength, double speed) {
        this.position = p;

        this.quaternion = new Quaternion(1, 0, 0, 0);

        this.R = new double[][] {{ 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }};

        this.focalLength = focalLength;
        this.speed = speed;
    }

    public Camera3DProjection() {
        this(new Vec3D(), 289, 0.1);
    }

    @Nullable
    public Vec2D projectPoint(Vec3D p) {
        double P1x = p.x - this.position.x;
        double P1y = p.y - this.position.y;
        double P1z = p.z - this.position.z;

        double[][] R_inv = MathHelper.matrixTranspose(R);

        double P2x = (R_inv[0][0]*P1x) + (R_inv[0][1]*P1y) + (R_inv[0][2]*P1z);
        double P2y = (R_inv[1][0]*P1x) + (R_inv[1][1]*P1y) + (R_inv[1][2]*P1z);
        double P2z = (R_inv[2][0]*P1x) + (R_inv[2][1]*P1y) + (R_inv[2][2]*P1z);

        if (P2z > 0) {
            return new Vec2D(
                    this.focalLength * P2x / P2z,
                    this.focalLength * P2y / P2z
            );
        }
        else {
            return null;
        }
    }

    public void setPosition(Vec3D p) {
        this.position = p;
    }

    public void translatePosition(Vec3D m) {
        this.position.x += (this.R[0][0] * this.speed * m.x) + (this.R[0][1] * this.speed * m.y) + (this.R[0][2] * this.speed * m.z);
        this.position.y += (this.R[1][0] * this.speed * m.x) + (this.R[1][1] * this.speed * m.y) + (this.R[1][2] * this.speed * m.z);
        this.position.z += (this.R[2][0] * this.speed * m.x) + (this.R[2][1] * this.speed * m.y) + (this.R[2][2] * this.speed * m.z);
    }

    public void rotateView(double deltaAngleDegrees, double[] axis) {
        // Fix: Rotation is now relative to camera space not to world space
        // Transform world space axis to camera space axis by multiplying with the rotation matrix
        // https://en.wikipedia.org/wiki/Change_of_basis#Example
        // https://en.wikipedia.org/wiki/Rotation_matrix

        double[] cameraAxis = new double[] {
                this.R[0][0] * axis[0] + this.R[0][1] * axis[1] + this.R[0][2] * axis[2],
                this.R[1][0] * axis[0] + this.R[1][1] * axis[1] + this.R[1][2] * axis[2],
                this.R[2][0] * axis[0] + this.R[2][1] * axis[1] + this.R[2][2] * axis[2]
        };

        double deltaAngle = (deltaAngleDegrees * Math.PI / 180) / 2;
        Quaternion deltaQ = new Quaternion(
                Math.cos(deltaAngle),
                cameraAxis[0] * Math.sin(deltaAngle),
                cameraAxis[1] * Math.sin(deltaAngle),
                cameraAxis[2] * Math.sin(deltaAngle)
        );

        this.quaternion = MathHelper.quaternionNormalize(MathHelper.quaternionMultiply(this.quaternion, deltaQ));
        this.R = MathHelper.quaternionToMatrix(this.quaternion);
    }

    public double getX() {
        return this.position.x;
    }

    public double getY() {
        return this.position.y;
    }

    public double getZ() {
        return this.position.z;
    }

    public Quaternion getQuaternion() { return this.quaternion; }

    public double getFocalLength() {
        return this.focalLength;
    }
}

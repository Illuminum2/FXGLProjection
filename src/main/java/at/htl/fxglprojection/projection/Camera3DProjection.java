package at.htl.fxglprojection.projection;

import org.jetbrains.annotations.Nullable;

import com.almasb.fxgl.dsl.FXGL;

public class Camera3DProjection {
    private Vec3D position;
    private Quaternion quaternion;

    private double[][] R;
    private double[][] R_inv;

    private double focalLength;
    private double movementSpeed;
    private double rotationSpeed;

    public Camera3DProjection(Vec3D p, double focalLength, double movementSpeed, double rotationSpeed) {
        this.position = p;

        this.quaternion = new Quaternion(1, 0, 0, 0);

        this.R = new double[][] {{ 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }};
        this.R_inv = new double[][] {{ 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }};

        this.focalLength = focalLength;
        this.movementSpeed = movementSpeed;
        this.rotationSpeed = rotationSpeed;
    }

    public Camera3DProjection() {
        this(new Vec3D(), 289, 256, 180);
    }

    @Nullable
    public Vec3D projectPoint(Vec3D p) {
        Vec3D pCamera = toCameraSpace(p);

        if (pCamera.z > 0) {
            return new Vec3D(
                    this.focalLength * pCamera.x / pCamera.z,
                    this.focalLength * pCamera.y / pCamera.z,
                    pCamera.z
            );
        }
        else {
            return null;
        }
    }

    public Vec3D toCameraSpace(Vec3D p) {
        return toCameraSpaceDirection(p.subtract(this.position));
    }

    public Vec3D toCameraSpaceDirection(Vec3D v) {
        return new Vec3D(
                (R_inv[0][0] * v.x) + (R_inv[0][1] * v.y) + (R_inv[0][2] * v.z),
                (R_inv[1][0] * v.x) + (R_inv[1][1] * v.y) + (R_inv[1][2] * v.z),
                (R_inv[2][0] * v.x) + (R_inv[2][1] * v.y) + (R_inv[2][2] * v.z)
        );
    }

    public void setPosition(Vec3D p) {
        this.position = p;
    }

    public void translatePosition(Vec3D m) {
        translatePosition(this.movementSpeed, m);
    }

    public void translatePosition(double speed, Vec3D m) {
        speed *= FXGL.tpf();

        this.position.x += (this.R[0][0] * speed * m.x) + (this.R[0][1] * speed * m.y) + (this.R[0][2] * speed * m.z);
        this.position.y += (this.R[1][0] * speed * m.x) + (this.R[1][1] * speed * m.y) + (this.R[1][2] * speed * m.z);
        this.position.z += (this.R[2][0] * speed * m.x) + (this.R[2][1] * speed * m.y) + (this.R[2][2] * speed * m.z);
    }

    public void rotateView(Vec3D axis) {
        rotateView(this.rotationSpeed * FXGL.tpf(), axis);
    }

    public void rotateView(double deltaAngleDegrees, Vec3D axis) {
        double deltaAngle = (deltaAngleDegrees * Math.PI / 180) / 2;
        Quaternion deltaQ = new Quaternion(
                Math.cos(deltaAngle),
                axis.x * Math.sin(deltaAngle),
                axis.y * Math.sin(deltaAngle),
                axis.z * Math.sin(deltaAngle)
        );

        // Post-multiply because the axis is already in camera space.
        this.quaternion = MathHelper.quaternionNormalize(MathHelper.quaternionMultiply(this.quaternion, deltaQ));
        this.R = MathHelper.quaternionToMatrix(this.quaternion);
        this.R_inv = MathHelper.matrixTranspose(this.R);
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

    public double getMovementSpeed() { return this.movementSpeed; }

    public double getRotationSpeed() { return this.rotationSpeed; }

    public void setFocalLength(double focalLength) { this.focalLength = focalLength; }

    public void setMovementSpeed(double speed) { this.movementSpeed = speed; }

    public void setRotationSpeed(double speed) { this.rotationSpeed = speed; }
}

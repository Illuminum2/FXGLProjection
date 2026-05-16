package at.htl.fxglprojection.projection;

import org.jetbrains.annotations.Nullable;

public class Camera3DProjection {
    private Vec3D position;
    private Quaternion quaternion;

    private double[][] R;

    private double focalLength;
    private double movementSpeed;
    private double rotationSpeed;

    public Camera3DProjection(Vec3D p, double focalLength, double movementSpeed, double rotationSpeed) {
        this.position = p;

        this.quaternion = new Quaternion(1, 0, 0, 0);

        this.R = new double[][] {{ 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }};

        this.focalLength = focalLength;
        this.movementSpeed = movementSpeed;
        this.rotationSpeed = rotationSpeed;
    }

    public Camera3DProjection() {
        this(new Vec3D(), 289, 2, 1.5);
    }

    @Nullable
    public Vec3D projectPoint(Vec3D p) {
        double P1x = p.x - this.position.x;
        double P1y = p.y - this.position.y;
        double P1z = p.z - this.position.z;

        double[][] R_inv = MathHelper.matrixTranspose(R);

        double P2x = (R_inv[0][0]*P1x) + (R_inv[0][1]*P1y) + (R_inv[0][2]*P1z);
        double P2y = (R_inv[1][0]*P1x) + (R_inv[1][1]*P1y) + (R_inv[1][2]*P1z);
        double P2z = (R_inv[2][0]*P1x) + (R_inv[2][1]*P1y) + (R_inv[2][2]*P1z);

        if (P2z > 0) {
            return new Vec3D(
                    this.focalLength * P2x / P2z,
                    this.focalLength * P2y / P2z,
                    P2z
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
        translatePosition(this.movementSpeed, m);
    }

    public void translatePosition(double speed, Vec3D m) {
        this.position.x += (this.R[0][0] * speed * m.x) + (this.R[0][1] * speed * m.y) + (this.R[0][2] * speed * m.z);
        this.position.y += (this.R[1][0] * speed * m.x) + (this.R[1][1] * speed * m.y) + (this.R[1][2] * speed * m.z);
        this.position.z += (this.R[2][0] * speed * m.x) + (this.R[2][1] * speed * m.y) + (this.R[2][2] * speed * m.z);
    }

    public void rotateView(Vec3D axis) {
        rotateView(this.rotationSpeed, axis);
    }

    public void rotateView(double deltaAngleDegrees, Vec3D axis) {
        double deltaAngle = (deltaAngleDegrees * Math.PI / 180) / 2;
        Quaternion deltaQ = new Quaternion(
                Math.cos(deltaAngle),
                axis.x * Math.sin(deltaAngle),
                axis.y * Math.sin(deltaAngle),
                axis.z * Math.sin(deltaAngle)
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

    public double getMovementSpeed() { return this.movementSpeed; }

    public double getRotationSpeed() { return this.rotationSpeed; }

    public void setFocalLength(double focalLength) { this.focalLength = focalLength; }

    public void setMovementSpeed(double speed) { this.movementSpeed = speed; }

    public void setRotationSpeed(double speed) { this.rotationSpeed = speed; }
}
